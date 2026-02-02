import java.util.Scanner;

/**
 * CinemaApp - Handles the menu and main. Uses CinemaManager for all logic.
 */
public class CinemaApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of rows: ");
        int rows = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter number of columns: ");
        int cols = Integer.parseInt(scanner.nextLine().trim());
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
            String input = scanner.nextLine().trim();
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
                case 3:
                    System.out.print("Enter row index (0-" + (cinema.getRows() - 1) + "): ");
                    try {
                        int row = Integer.parseInt(scanner.nextLine().trim());
                        boolean usable = cinema.isRowUsable(row);
                        System.out.println("Row " + row + " is " + (usable ? "usable" : "not usable") + ".");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid row.");
                    }
                    break;
                case 4:
                    System.out.print("Enter row index (0-" + (cinema.getRows() - 1) + "): ");
                    try {
                        int row = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                        int size = Integer.parseInt(scanner.nextLine().trim());
                        boolean can = cinema.canSeatGroupInRow(row, size);
                        System.out.println("Row " + row + " can " + (can ? "" : "not ") + "seat a group of " + size + ".");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                    break;
                case 5:
                    System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                    try {
                        int size = Integer.parseInt(scanner.nextLine().trim());
                        int best = cinema.suggestBestRow(size);
                        if (best == -1) {
                            System.out.println("No row can seat a group of " + size + ".");
                        } else {
                            System.out.println("Suggested best row: " + best + " (most available seats).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid group size.");
                    }
                    break;
                case 6:
                    System.out.print("Enter group size (1-" + cinema.getCols() + "): ");
                    try {
                        int size = Integer.parseInt(scanner.nextLine().trim());
                        if (cinema.bookGroup(size)) {
                            System.out.println("Group of " + size + " booked. Updated seating:");
                            cinema.printSeating();
                        } else {
                            System.out.println("Could not book a group of " + size + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid group size.");
                    }
                    break;
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
