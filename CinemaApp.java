import java.util.Scanner;

/**
 * CinemaApp - Handles the menu and main. Uses CinemaManager for all logic.
 * Input validation prevents crashes on invalid input.
 */
public class CinemaApp {

    /** Reads a positive int with retry on invalid input; never throws. */
    private static int readPositiveInt(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();
            try {
                int n = Integer.parseInt(line);
                if (n > 0) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println(errorMsg);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows = readPositiveInt(scanner, "Enter number of rows: ", "Invalid. Enter a positive number.");
        int cols = readPositiveInt(scanner, "Enter number of columns: ", "Invalid. Enter a positive number.");
        CinemaManager cinema = new CinemaManager(rows, cols);

        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("   CINEMA SEATING - Menu");
            System.out.println("========================================");
            System.out.println("1. Print seating");
            System.out.println("2. Show counts and occupancy");
            System.out.println("3. Check if a row is usable");
            System.out.println("4. Check if a row can seat a group");
            System.out.println("5. Suggest best row");
            System.out.println("6. Book a group and show updated seating");
            System.out.println("7. Exit");
            System.out.print("Choose an option (1-7): ");
            String input = scanner.nextLine();
            if (input == null) input = "";
            input = input.trim();
            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number 1-7.");
                continue;
            }

            switch (choice) {
                case 1:
                    cinema.printSeating();
                    break;
                case 2:
                    System.out.println("\n--- Counts and occupancy ---");
                    System.out.println("Available: " + cinema.getAvailableCount());
                    System.out.println("Booked: " + cinema.getBookedCount());
                    System.out.println("Broken: " + cinema.getBrokenCount());
                    System.out.println("VIP: " + cinema.getVIPCount());
                    System.out.printf("Occupancy rate: %.1f%%%n%n", cinema.getOccupancyRate());
                    break;
                case 3: {
                    System.out.print("Enter row index (0-" + (cinema.getRows() - 1) + "): ");
                    String line = scanner.nextLine();
                    if (line == null) line = "";
                    try {
                        int row = Integer.parseInt(line.trim());
                        if (row >= 0 && row < cinema.getRows()) {
                            boolean usable = cinema.isRowUsable(row);
                            System.out.println("Row " + row + " is " + (usable ? "usable" : "not usable") + ".");
                        } else {
                            System.out.println("Invalid row. Enter 0 to " + (cinema.getRows() - 1) + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter a number.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Enter row index (0-" + (cinema.getRows() - 1) + "): ");
                    String lineRow = scanner.nextLine();
                    if (lineRow == null) lineRow = "";
                    try {
                        int row = Integer.parseInt(lineRow.trim());
                        if (row < 0 || row >= cinema.getRows()) {
                            System.out.println("Invalid row. Enter 0 to " + (cinema.getRows() - 1) + ".");
                            break;
                        }
                        System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                        String lineSize = scanner.nextLine();
                        if (lineSize == null) lineSize = "";
                        int size = Integer.parseInt(lineSize.trim());
                        if (size < 1 || size > cinema.getCols()) {
                            System.out.println("Invalid group size. Enter 1 to " + cinema.getCols() + ".");
                            break;
                        }
                        boolean can = cinema.canSeatGroupInRow(row, size);
                        System.out.println("Row " + row + " can " + (can ? "" : "not ") + "seat a group of " + size + ".");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter numbers.");
                    }
                    break;
                }
                case 5: {
                    System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                    String line5 = scanner.nextLine();
                    if (line5 == null) line5 = "";
                    try {
                        int size = Integer.parseInt(line5.trim());
                        if (size < 1 || size > cinema.getCols()) {
                            System.out.println("Invalid group size. Enter 1 to " + cinema.getCols() + ".");
                            break;
                        }
                        int best = cinema.suggestBestRow(size);
                        if (best == -1) {
                            System.out.println("No row can seat a group of " + size + ".");
                        } else {
                            System.out.println("Suggested best row: " + best + " (most available seats).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter a number.");
                    }
                    break;
                }
                case 6: {
                    System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                    String line6 = scanner.nextLine();
                    if (line6 == null) line6 = "";
                    try {
                        int size = Integer.parseInt(line6.trim());
                        if (size < 1 || size > cinema.getCols()) {
                            System.out.println("Invalid group size. Enter 1 to " + cinema.getCols() + ".");
                            break;
                        }
                        if (cinema.bookGroup(size)) {
                            System.out.println("Group of " + size + " booked. Updated seating:");
                            cinema.printSeating();
                        } else {
                            System.out.println("Could not book a group of " + size + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter a number.");
                    }
                    break;
                }
                case 7:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }
        scanner.close();
    }
}
