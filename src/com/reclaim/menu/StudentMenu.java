package com.reclaim.menu;

import com.reclaim.enums.ItemCategory;
import com.reclaim.model.Claim;
import com.reclaim.model.Item;
import com.reclaim.model.OwnershipProof;
import com.reclaim.model.Student;
import com.reclaim.model.User;
import com.reclaim.repository.ItemRepository;
import com.reclaim.service.ClaimService;
import com.reclaim.service.NotificationService;
import com.reclaim.service.SearchService;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console menu providing student actions for searching items, submitting claims, and viewing notifications.
 */
public class StudentMenu {

    private final SearchService searchService;
    private final ClaimService claimService;
    private final NotificationService notificationService;
    private final ItemRepository itemRepository;

    public StudentMenu(SearchService searchService, ClaimService claimService,
                       NotificationService notificationService, ItemRepository itemRepository) {
        this.searchService = searchService;
        this.claimService = claimService;
        this.notificationService = notificationService;
        this.itemRepository = itemRepository;
    }

    public void displayMenu(User loggedInUser, Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Error: No student user is currently logged in.");
            return;
        }

        int studentId = loggedInUser.getUserId();
        boolean running = true;

        while (running) {
            System.out.println("\n================================================================================");
            System.out.println("                   STUDENT PORTAL - reClaim Lost & Found                        ");
            System.out.println("                   Welcome, " + loggedInUser.getName() + " (ID: " + studentId + ")");
            System.out.println("================================================================================");
            System.out.println(" 1. Search all available items");
            System.out.println(" 2. Search items by name or keyword");
            System.out.println(" 3. Search by campus location");
            System.out.println(" 4. Search by category");
            System.out.println(" 5. View item details");
            System.out.println(" 6. Submit an ownership claim");
            System.out.println(" 7. View my submitted claims");
            System.out.println(" 8. View notifications");
            System.out.println(" 9. Mark notifications as read");
            System.out.println(" 10. Logout");
            System.out.println("================================================================================");
            System.out.print("Enter choice (1-10): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleSearchAllAvailable();
                    break;
                case "2":
                    handleSearchByNameOrKeyword(scanner);
                    break;
                case "3":
                    handleSearchByLocation(scanner);
                    break;
                case "4":
                    handleSearchByCategory(scanner);
                    break;
                case "5":
                    handleViewItemDetails(scanner);
                    break;
                case "6":
                    handleSubmitClaim(studentId, scanner);
                    break;
                case "7":
                    handleViewMyClaims(studentId);
                    break;
                case "8":
                    handleViewNotifications(studentId);
                    break;
                case "9":
                    handleMarkNotificationRead(scanner);
                    break;
                case "10":
                    System.out.println("Logging out from Student Portal. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 10.");
                    break;
            }
        }
    }

    private void handleSearchAllAvailable() {
        ArrayList<Item> available = itemRepository.getAvailableItems();
        searchService.displaySearchResults(available);
    }

    private void handleSearchByNameOrKeyword(Scanner scanner) {
        System.out.print("Enter item name or search keyword: ");
        String query = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByKeyword(query);
        searchService.displaySearchResults(results);
    }

    private void handleSearchByLocation(Scanner scanner) {
        System.out.print("Enter campus location (e.g., Library, Cafeteria, Tech Park): ");
        String loc = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByLocation(loc);
        searchService.displaySearchResults(results);
    }

    private void handleSearchByCategory(Scanner scanner) {
        System.out.println("Available categories: ELECTRONICS, DOCUMENT, PERSONAL_BELONGING, ACCESSORY, OTHER");
        System.out.print("Enter category name: ");
        String catName = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByCategory(catName);
        searchService.displaySearchResults(results);
    }

    private void handleViewItemDetails(Scanner scanner) {
        System.out.print("Enter Item ID to view details: ");
        try {
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            Item item = itemRepository.getItemById(itemId);
            if (item == null) {
                System.out.println("No item found with ID " + itemId + ".");
            } else {
                searchService.displaySearchResults(item);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Item ID format. Please enter a positive integer.");
        }
    }

    private void handleSubmitClaim(int studentId, Scanner scanner) {
        System.out.println("\n--- Submit Item Claim ---");
        System.out.print("Enter Item ID to claim: ");
        try {
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter item colour: ");
            String colour = scanner.nextLine().trim();
            System.out.print("Enter item brand: ");
            String brand = scanner.nextLine().trim();
            System.out.print("Enter identifying description (unique scratches, stickers, marks): ");
            String desc = scanner.nextLine().trim();
            System.out.print("Enter additional ownership proof (purchase receipt, student roll number): ");
            String additional = scanner.nextLine().trim();

            OwnershipProof proof = new OwnershipProof(colour, brand, desc, additional);
            Claim claim = claimService.submitClaim(itemId, studentId, proof);
            System.out.println("\nSUCCESS: Claim #" + claim.getClaimId() + " submitted successfully with PENDING status.");
            System.out.println("The finder has been notified for ownership verification.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Item ID must be a numeric value.");
        } catch (Exception e) {
            System.out.println("Error submitting claim: " + e.getMessage());
        }
    }

    private void handleViewMyClaims(int studentId) {
        ArrayList<Claim> myClaims = claimService.viewStudentClaims(studentId);
        if (myClaims.isEmpty()) {
            System.out.println("You have not submitted any claims yet.");
            return;
        }

        System.out.println("\n================================================================================");
        System.out.println("                        MY SUBMITTED CLAIMS (" + myClaims.size() + ")                                 ");
        System.out.println("================================================================================");
        for (Claim c : myClaims) {
            c.displayClaimDetails();
        }
    }

    private void handleViewNotifications(int studentId) {
        if (notificationService != null) {
            notificationService.displayNotifications(studentId);
        } else {
            System.out.println("Notification service is currently unavailable.");
        }
    }

    private void handleMarkNotificationRead(Scanner scanner) {
        System.out.print("Enter Notification ID to mark as read: ");
        try {
            int notifId = Integer.parseInt(scanner.nextLine().trim());
            if (notificationService != null && notificationService.markNotificationAsRead(notifId)) {
                System.out.println("Notification #" + notifId + " marked as read.");
            } else {
                System.out.println("Notification #" + notifId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Notification ID format.");
        }
    }
}
