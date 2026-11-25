package ui;

import model.*;
import service.AdminService;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * AdminMenu provides the terminal interface for admin-only operations.
 *
 * Access is guarded at entry: only a user with role ADMIN can use this menu.
 * Uses Scanner for input and System.out.println for output.
 *
 * OOP concept demonstrated: Encapsulation, role-based access control
 */
public class AdminMenu {

    /**
     * Shows the admin dashboard.
     *
     * @param admin        The currently logged-in user (must be an Admin)
     * @param adminService The service that handles admin business logic
     * @param scanner      The shared Scanner for reading user input
     */
    public static void show(User admin, AdminService adminService, Scanner scanner) {

        // --- Security check: reject non-admin users ---
        if (admin == null || admin.getRole() != UserRole.ADMIN) {
            System.out.println("Access denied. Only admins can view this dashboard.");
            return;
        }

        System.out.println("\nWelcome, Admin " + admin.getName() + "!");

        boolean running = true;

        while (running) {

            // Print the menu
            System.out.println("\n========== reClaim ADMIN DASHBOARD ==========");
            System.out.println("1. View All Users");
            System.out.println("2. View All Items");
            System.out.println("3. View All Lost Reports");
            System.out.println("4. View All Claims");
            System.out.println("5. View All Handovers");
            System.out.println("6. Search User by ID");
            System.out.println("7. Search Item by ID");
            System.out.println("8. View Statistics");
            System.out.println("9. Logout");
            System.out.println("=============================================");
            System.out.print("Enter your choice: ");

            // Read the user's choice safely
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                continue;
            }

            switch (choice) {

                case 1:
                    // View all users
                    System.out.println("\n--- All Registered Users ---");
                    ArrayList<User> users = adminService.viewAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("No users registered yet.");
                    } else {
                        for (User u : users) {
                            System.out.println(u);
                        }
                    }
                    break;

                case 2:
                    // View all items
                    System.out.println("\n--- All Reported Found Items ---");
                    ArrayList<Item> items = adminService.viewAllItems();
                    if (items.isEmpty()) {
                        System.out.println("No items reported yet.");
                    } else {
                        for (Item item : items) {
                            System.out.println(item);
                        }
                    }
                    break;

                case 3:
                    // View all lost reports
                    System.out.println("\n--- All Lost Reports ---");
                    ArrayList<LostReport> reports = adminService.viewAllLostReports();
                    if (reports.isEmpty()) {
                        System.out.println("No lost reports submitted yet.");
                    } else {
                        for (LostReport r : reports) {
                            System.out.println(r);
                        }
                    }
                    break;

                case 4:
                    // View all claims
                    System.out.println("\n--- All Claims ---");
                    ArrayList<Claim> claims = adminService.viewAllClaims();
                    if (claims.isEmpty()) {
                        System.out.println("No claims submitted yet.");
                    } else {
                        for (Claim c : claims) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 5:
                    // View all handovers
                    System.out.println("\n--- All Handovers ---");
                    ArrayList<Handover> handovers = adminService.viewAllHandovers();
                    if (handovers.isEmpty()) {
                        System.out.println("No handover records found.");
                    } else {
                        for (Handover h : handovers) {
                            System.out.println(h);
                        }
                    }
                    break;

                case 6:
                    // Search user by ID
                    System.out.print("Enter User ID to search: ");
                    String userId = scanner.nextLine().trim();
                    if (userId.isEmpty()) {
                        System.out.println("User ID cannot be empty.");
                        break;
                    }
                    User foundUser = adminService.searchUserById(userId);
                    if (foundUser == null) {
                        System.out.println("No user found with ID: " + userId);
                    } else {
                        System.out.println("Found: " + foundUser);
                    }
                    break;

                case 7:
                    // Search item by ID
                    System.out.print("Enter Item ID to search: ");
                    String itemId = scanner.nextLine().trim();
                    if (itemId.isEmpty()) {
                        System.out.println("Item ID cannot be empty.");
                        break;
                    }
                    Item foundItem = adminService.searchItemById(itemId);
                    if (foundItem == null) {
                        System.out.println("No item found with ID: " + itemId);
                    } else {
                        System.out.println("Found: " + foundItem);
                    }
                    break;

                case 8:
                    // View statistics
                    adminService.printStatistics();
                    break;

                case 9:
                    // Logout
                    System.out.println("Logging out from Admin Dashboard. Goodbye, " + admin.getName() + "!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }
}
