package ui;

import model.*;
import service.*;
import util.InputHandler;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * StudentMenu provides the terminal interface for a logged-in student.
 *
 * Students can browse found items, submit ownership claims, view their
 * claim statuses, and report items they have lost.
 *
 * OOP concept demonstrated: Encapsulation, role-based menu
 */
public class StudentMenu {

    public static void show(User student, ItemService itemService,
                            ClaimService claimService,
                            LostReportService lostReportService,
                            Scanner scanner) {

        InputHandler input = new InputHandler(scanner);
        boolean running = true;

        while (running) {
            System.out.println("\n========== STUDENT MENU ==========");
            System.out.println("Logged in as: " + student.getName());
            System.out.println("1. Browse Available Items");
            System.out.println("2. Search Items");
            System.out.println("3. Submit a Claim");
            System.out.println("4. View My Claims");
            System.out.println("5. Report a Lost Item");
            System.out.println("6. View My Lost Reports");
            System.out.println("7. Logout");
            System.out.println("===================================");

            int choice = input.readIntInRange("Enter choice: ", 1, 7);

            switch (choice) {

                case 1:
                    // Browse available found items
                    System.out.println("\n--- Available Found Items ---");
                    ArrayList<Item> available = itemService.getAvailableItems();
                    if (available.isEmpty()) {
                        System.out.println("No items available right now.");
                    } else {
                        for (Item item : available) {
                            System.out.println(item);
                        }
                    }
                    break;

                case 2:
                    // Search by keyword
                    String keyword = input.readString("Enter search keyword: ");
                    ArrayList<Item> results = itemService.searchItems(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No items matched your search.");
                    } else {
                        for (Item item : results) {
                            System.out.println(item);
                        }
                    }
                    break;

                case 3:
                    // Submit a claim
                    String itemId  = input.readString("Enter Item ID to claim: ");
                    String remarks = input.readOptionalString("Enter your ownership proof/remarks (optional): ");
                    claimService.submitClaim(itemId, student.getUserId(), remarks);
                    break;

                case 4:
                    // View own claims
                    System.out.println("\n--- Your Claims ---");
                    ArrayList<Claim> myClaims = claimService.getClaimsByStudent(student.getUserId());
                    if (myClaims.isEmpty()) {
                        System.out.println("You have not submitted any claims yet.");
                    } else {
                        for (Claim c : myClaims) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 5:
                    // Report a lost item
                    String lostName  = input.readString("Item name: ");
                    String lostDesc  = input.readString("Description: ");
                    String lostLoc   = input.readString("Last seen location: ");
                    lostReportService.reportLostItem(student.getUserId(), lostName, lostDesc, lostLoc);
                    break;

                case 6:
                    // View own lost reports
                    System.out.println("\n--- Your Lost Reports ---");
                    ArrayList<LostReport> myReports = lostReportService.getLostReportsByUser(student.getUserId());
                    if (myReports.isEmpty()) {
                        System.out.println("You have no lost reports.");
                    } else {
                        for (LostReport r : myReports) {
                            System.out.println(r);
                        }
                    }
                    break;

                case 7:
                    System.out.println("Logging out... Goodbye, " + student.getName() + "!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
