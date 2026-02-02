/**
 * CinemaManager - All cinema seating logic. Seat values: 0=available, 1=booked, 2=broken, 3=VIP.
 */
public class CinemaManager {

    public static final int AVAILABLE = 0;
    public static final int BOOKED = 1;
    public static final int BROKEN = 2;
    public static final int VIP = 3;

    private final int rows;
    private final int cols;
    private final int[][] seats;

    /**
     * Constructor: takes rows and columns, randomly assigns seat values.
     * Most seats available, some booked, a few broken, a few VIP.
     * VIP seats only in the middle 4 rows.
     */
    public CinemaManager(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.seats = new int[rows][cols];

        int midStart = Math.max(0, (rows - 4) / 2);
        int midEnd = Math.min(rows, midStart + 4);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int roll = (int) (Math.random() * 100);
                boolean inMiddleFour = (r >= midStart && r < midEnd);

                if (roll < 65) {
                    seats[r][c] = AVAILABLE;
                } else if (roll < 90) {
                    seats[r][c] = BOOKED;
                } else if (roll < 95) {
                    seats[r][c] = BROKEN;
                } else {
                    seats[r][c] = inMiddleFour ? VIP : AVAILABLE;
                }
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    /** Returns number of available seats (value 0). */
    public int getAvailableCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == AVAILABLE) count++;
            }
        }
        return count;
    }

    /** Returns number of booked seats (value 1). */
    public int getBookedCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == BOOKED) count++;
            }
        }
        return count;
    }

    /** Returns number of broken seats (value 2). */
    public int getBrokenCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == BROKEN) count++;
            }
        }
        return count;
    }

    /** Returns number of VIP seats (value 3). */
    public int getVIPCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == VIP) count++;
            }
        }
        return count;
    }

    /** Returns occupancy rate as a percentage (0.0–100.0). Booked + VIP count as occupied. */
    public double getOccupancyRate() {
        int total = rows * cols;
        if (total == 0) return 0.0;
        int occupied = getBookedCount() + getVIPCount();
        return occupied * 100.0 / total;
    }

    /**
     * Returns true if the row is usable: at least one available seat, not all broken,
     * and not only booked and VIP. Uses at least two boolean variables.
     */
    public boolean isRowUsable(int row) {
        if (row < 0 || row >= rows) return false;

        boolean hasAvailable = false;
        boolean hasNonBroken = false;

        for (int c = 0; c < cols; c++) {
            int val = seats[row][c];
            if (val == AVAILABLE) hasAvailable = true;
            if (val != BROKEN) hasNonBroken = true;
        }

        boolean notAllBroken = hasNonBroken;
        boolean notOnlyBookedAndVIP = hasAvailable;

        return notAllBroken && notOnlyBookedAndVIP;
    }

    /**
     * Returns true if the row can seat a group of groupSize: has a contiguous block
     * of seats that are available or VIP, and no seat in that block is next to a broken seat.
     * Uses compound boolean conditions.
     */
    public boolean canSeatGroupInRow(int row, int groupSize) {
        if (row < 0 || row >= rows || groupSize <= 0 || groupSize > cols) return false;

        for (int start = 0; start <= cols - groupSize; start++) {
            boolean allSeatsUsable = true;
            boolean noneNextToBroken = true;
            for (int c = start; c < start + groupSize; c++) {
                int val = seats[row][c];
                boolean seatUsable = (val == AVAILABLE || val == VIP);
                boolean leftBroken = (c > 0 && seats[row][c - 1] == BROKEN);
                boolean rightBroken = (c < cols - 1 && seats[row][c + 1] == BROKEN);
                boolean nextToBroken = leftBroken || rightBroken;

                allSeatsUsable = allSeatsUsable && seatUsable;
                noneNextToBroken = noneNextToBroken && !nextToBroken;
            }
            if (allSeatsUsable && noneNextToBroken) return true;
        }
        return false;
    }

    /**
     * Returns the row index with the most available seats that can seat the group.
     * If tied, returns the lowest row index. No center preference.
     */
    public int suggestBestRow(int groupSize) {
        if (groupSize <= 0 || groupSize > cols) return -1;

        int bestRow = -1;
        int mostAvailable = -1;

        for (int r = 0; r < rows; r++) {
            if (!canSeatGroupInRow(r, groupSize)) continue;
            int avail = 0;
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == AVAILABLE) avail++;
            }
            if (avail > mostAvailable) {
                mostAvailable = avail;
                bestRow = r;
            }
        }
        return bestRow;
    }

    /**
     * Finds the first valid starting column in the row for a block of groupSize
     * (available or VIP, not next to broken). Returns -1 if none.
     */
    private int findBlockStartInRow(int row, int groupSize) {
        for (int start = 0; start <= cols - groupSize; start++) {
            boolean allSeatsUsable = true;
            boolean noneNextToBroken = true;
            for (int c = start; c < start + groupSize; c++) {
                int val = seats[row][c];
                boolean seatUsable = (val == AVAILABLE || val == VIP);
                boolean leftBroken = (c > 0 && seats[row][c - 1] == BROKEN);
                boolean rightBroken = (c < cols - 1 && seats[row][c + 1] == BROKEN);
                allSeatsUsable = allSeatsUsable && seatUsable;
                noneNextToBroken = noneNextToBroken && !leftBroken && !rightBroken;
            }
            if (allSeatsUsable && noneNextToBroken) return start;
        }
        return -1;
    }

    /**
     * Books a group of the given size: uses suggestBestRow, books the first valid block,
     * converts available to booked, leaves VIP unchanged. Returns true if booked.
     */
    public boolean bookGroup(int groupSize) {
        if (groupSize <= 0 || groupSize > cols) return false;

        int row = suggestBestRow(groupSize);
        if (row == -1) return false;

        int start = findBlockStartInRow(row, groupSize);
        if (start == -1) return false;

        for (int c = start; c < start + groupSize; c++) {
            if (seats[row][c] == AVAILABLE) seats[row][c] = BOOKED;
        }
        return true;
    }

    /**
     * Prints seating: column numbers at top, each row as "Row r: ...", legend at bottom.
     */
    public void printSeating() {
        System.out.println("\n--- SEATING ---");
        // Column numbers at the top
        System.out.print("     ");
        for (int c = 0; c < cols; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        // Each row as "Row r: ..."
        for (int r = 0; r < rows; r++) {
            System.out.print("Row " + r + ":");
            for (int c = 0; c < cols; c++) {
                System.out.print(" " + seats[r][c]);
            }
            System.out.println();
        }
        // Legend at the bottom
        System.out.println("Legend: 0=available, 1=booked, 2=broken, 3=VIP");
        System.out.println();
    }
}
