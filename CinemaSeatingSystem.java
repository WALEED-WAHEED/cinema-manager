import java.util.Scanner;

/**
 * Cinema Seating System - A menu-driven program that uses a 2D array to represent
 * cinema seats. Supports availability checking, VIP seating, and group bookings.
 * Designed for high school/college level Java.
 */
public class CinemaSeatingSystem {

    // Seat status constants: 0 = available, 1 = regular booked, 2 = VIP booked
    public static final int AVAILABLE = 0;
    public static final int REGULAR_BOOKED = 1;
    public static final int VIP_BOOKED = 2;

    // Cinema dimensions
    private static final int ROWS = 5;
    private static final int COLS = 8;
    // First two rows (0 and 1) are VIP rows
    private static final int VIP_ROW_END = 1;

    // 2D array storing seat status for each row and column
    private int[][] seats;

    private Scanner scanner;

    /**
     * Constructor: initializes the 2D seating array (all seats available).
     */
    public CinemaSeatingSystem() {
        seats = new int[ROWS][COLS];
        scanner = new Scanner(System.in);
        // All seats start as available
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                seats[r][c] = AVAILABLE;
            }
        }
    }

    /**
     * Returns true if the row has at least one available seat and is not fully booked.
     * Uses boolean logic: row is usable if we can find any available seat in it.
     */
    public boolean isRowUsable(int row) {
        if (row < 0 || row >= ROWS) {
            return false;
        }
        for (int c = 0; c < COLS; c++) {
            if (seats[row][c] == AVAILABLE) {
                return true;  // at least one seat is free
            }
        }
        return false;  // row is full
    }

    /**
     * Returns true if the given row has a contiguous block of at least groupSize
     * available seats. Uses a loop to find the longest contiguous available block.
     */
    public boolean canSeatGroupInRow(int row, int groupSize) {
        if (row < 0 || row >= ROWS || groupSize <= 0 || groupSize > COLS) {
            return false;
        }
        int contiguousCount = 0;
        for (int c = 0; c < COLS; c++) {
            if (seats[row][c] == AVAILABLE) {
                contiguousCount++;
                if (contiguousCount >= groupSize) {
                    return true;
                }
            } else {
                contiguousCount = 0;  // reset when we hit a booked seat
            }
        }
        return false;
    }

    /**
     * Finds the starting column of the first contiguous block of at least groupSize
     * seats in the given row. Returns -1 if no such block exists.
     */
    private int findContiguousStartInRow(int row, int groupSize) {
        int contiguousCount = 0;
        int startCol = 0;
        for (int c = 0; c < COLS; c++) {
            if (seats[row][c] == AVAILABLE) {
                if (contiguousCount == 0) {
                    startCol = c;
                }
                contiguousCount++;
                if (contiguousCount >= groupSize) {
                    return startCol;
                }
            } else {
                contiguousCount = 0;
            }
        }
        return -1;
    }

    /**
     * Suggests the best row for a group of the given size. Prefers rows that have
     * a contiguous block, with a preference for middle rows (better viewing).
     * Returns the row index, or -1 if no row can fit the group.
     */
    public int suggestBestRow(int groupSize) {
        if (groupSize <= 0 || groupSize > COLS) {
            return -1;
        }
        int middleRow = ROWS / 2;  // prefer center of cinema
        int bestRow = -1;
        int bestDistance = ROWS + 1;  // want row closest to middle

        for (int r = 0; r < ROWS; r++) {
            if (canSeatGroupInRow(r, groupSize)) {
                int distanceFromMiddle = Math.abs(r - middleRow);
                if (distanceFromMiddle < bestDistance) {
                    bestDistance = distanceFromMiddle;
                    bestRow = r;
                }
            }
        }
        return bestRow;
    }

    /**
     * Books a group of seats in the given row starting at startCol. Marks seats as
     * VIP or regular based on isVip and whether the row is a VIP row. Updates the
     * 2D seating array. Returns true if booking succeeded, false otherwise.
     */
    public boolean bookGroup(int row, int startCol, int groupSize, boolean isVip) {
        // Validate: row and column in range, group fits from startCol, seats are available
        if (row < 0 || row >= ROWS || startCol < 0 || startCol >= COLS) {
            return false;
        }
        if (groupSize <= 0 || startCol + groupSize > COLS) {
            return false;
        }
        // Check all seats in range are available
        for (int c = startCol; c < startCol + groupSize; c++) {
            if (seats[row][c] != AVAILABLE) {
                return false;
            }
        }
        // VIP rows: rows 0 and 1; use VIP_BOOKED if booking in VIP row and isVip
        int status = (row <= VIP_ROW_END && isVip) ? VIP_BOOKED : REGULAR_BOOKED;
        for (int c = startCol; c < startCol + groupSize; c++) {
            seats[row][c] = status;
        }
        return true;
    }

    /**
     * Displays the seating layout. Uses nested loops to print the 2D array.
     * R = regular booked, V = VIP booked, _ = available.
     */
    public void displaySeats() {
        System.out.println("\n--- SEATING LAYOUT ---");
        System.out.println("Row  | Seats (R=Regular, V=VIP, _=Available)");
        System.out.println("-----|----------------------------------------");
        for (int r = 0; r < ROWS; r++) {
            System.out.print("  " + r + "  | ");
            for (int c = 0; c < COLS; c++) {
                if (seats[r][c] == AVAILABLE) {
                    System.out.print("_ ");
                } else if (seats[r][c] == REGULAR_BOOKED) {
                    System.out.print("R ");
                } else {
                    System.out.print("V ");
                }
            }
            String rowLabel = (r <= VIP_ROW_END) ? " (VIP)" : "";
            System.out.println(rowLabel);
        }
        System.out.println("-----|----------------------------------------\n");
    }

    /**
     * Analyzes each row: counts available and booked seats, shows occupancy rate.
     */
    public void analyzeRows() {
        System.out.println("\n--- ROW ANALYSIS ---");
        int totalAvailable = 0;
        int totalSeats = ROWS * COLS;
        for (int r = 0; r < ROWS; r++) {
            int available = 0;
            int booked = 0;
            for (int c = 0; c < COLS; c++) {
                if (seats[r][c] == AVAILABLE) {
                    available++;
                } else {
                    booked++;
                }
            }
            totalAvailable += available;
            double rate = (COLS > 0) ? (booked * 100.0 / COLS) : 0;
            String vipLabel = (r <= VIP_ROW_END) ? " [VIP]" : "";
            System.out.printf("Row %d%s: %d available, %d booked (%.0f%% full)%n", r, vipLabel, available, booked, rate);
        }
        double overallRate = (totalSeats > 0) ? ((totalSeats - totalAvailable) * 100.0 / totalSeats) : 0;
        System.out.printf("%nTotal: %d available, %d booked. Overall occupancy: %.0f%%%n%n", totalAvailable, totalSeats - totalAvailable, overallRate);
    }

    /**
     * Runs the main menu loop. Validates user input to prevent crashes.
     */
    public void runMenu() {
        System.out.println("========================================");
        System.out.println("   CINEMA SEATING SYSTEM - Main Menu");
        System.out.println("========================================");
        boolean running = true;
        while (running) {
            System.out.println("1. Display seats");
            System.out.println("2. Analyze rows (occupancy)");
            System.out.println("3. Suggest best row for a group");
            System.out.println("4. Book a group");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a number.\n");
                continue;
            }
            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number 1-5.\n");
                continue;
            }
            switch (choice) {
                case 1:
                    displaySeats();
                    break;
                case 2:
                    analyzeRows();
                    break;
                case 3:
                    handleSuggestBestRow();
                    break;
                case 4:
                    handleBookGroup();
                    break;
                case 5:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.\n");
            }
        }
        scanner.close();
    }

    /**
     * Handles menu option 3: asks for group size, validates, then calls suggestBestRow
     * and prints the result.
     */
    private void handleSuggestBestRow() {
        System.out.print("Enter group size (1-" + COLS + "): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Invalid input.\n");
            return;
        }
        int size;
        try {
            size = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.\n");
            return;
        }
        if (size < 1 || size > COLS) {
            System.out.println("Group size must be between 1 and " + COLS + ".\n");
            return;
        }
        int row = suggestBestRow(size);
        if (row == -1) {
            System.out.println("No row can fit a group of " + size + " together.\n");
        } else {
            int startCol = findContiguousStartInRow(row, size);
            System.out.println("Suggested row: " + row + " (seats " + startCol + "-" + (startCol + size - 1) + ").\n");
        }
    }

    /**
     * Handles menu option 4: asks for row, start seat, group size, and VIP preference.
     * Validates all input and updates the 2D array via bookGroup.
     */
    private void handleBookGroup() {
        System.out.print("Enter row (0-" + (ROWS - 1) + "): ");
        String rowInput = scanner.nextLine().trim();
        System.out.print("Enter starting seat number (0-" + (COLS - 1) + "): ");
        String colInput = scanner.nextLine().trim();
        System.out.print("Enter group size (1-" + COLS + "): ");
        String sizeInput = scanner.nextLine().trim();
        System.out.print("VIP booking? (y/n): ");
        String vipInput = scanner.nextLine().trim().toLowerCase();

        int row, startCol, size;
        try {
            row = Integer.parseInt(rowInput);
            startCol = Integer.parseInt(colInput);
            size = Integer.parseInt(sizeInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers. Booking cancelled.\n");
            return;
        }
        boolean isVip = vipInput.startsWith("y");
        if (row < 0 || row >= ROWS || startCol < 0 || startCol >= COLS || size < 1 || startCol + size > COLS) {
            System.out.println("Invalid row, seat, or size. Booking cancelled.\n");
            return;
        }
        if (bookGroup(row, startCol, size, isVip)) {
            System.out.println("Booking successful.\n");
        } else {
            System.out.println("Booking failed: one or more seats are not available.\n");
        }
    }

    /**
     * Entry point: creates the cinema system and runs the menu.
     */
    public static void main(String[] args) {
        CinemaSeatingSystem cinema = new CinemaSeatingSystem();
        cinema.runMenu();
    }
}
