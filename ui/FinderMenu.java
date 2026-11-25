package ui;

import model.*;
import service.*;
import util.InputHandler;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * FinderMenu provides the terminal interface for a logged-in finder.
 *
 * Finders can report items they found, review claims on their items,
 * approve/reject claims, and confirm handovers.
 *
 * OOP concept demonstrated: Encapsulation, role-based menu
 */
public class FinderMenu {

    public static void show(User finder, ItemService itemService,
                            ClaimService claimService,
                            HandoverService handoverService,
                            Scanner scanner) {

        InputHandler input = new InputHandler(scanner);
        boolean running = true;

        while (running) {
            System.out.println("\n========== FINDER MENU ==========");
            System.out.println("Logged in as: " + finder.getName());
            System.out.println("1. Report a Found Item");
            System.out.println("2. View My Reported Items");
            System.out.println("3. View Claims on My Items");
            System.out.println("4. Approve a Claim");
            System.out.println("5. Reject a Claim");
            System.out.println("6. Start Handover (after claim approved)");
            System.out.println("7. Confirm Physical Handover");
            System.out.println("8. View My Handovers");
            System.out.println("9. Logout");
            System.out.println("==================================");

            int choice = input.readIntInRange("Enter choice: ", 1, 9);

            switch (choice) {

                case 1:
                    // Report a found item
                    String title    = input.readString("Item title: ");
                    String desc     = input.readString("Description: ");
                    String location = input.readString("Where was it found?: ");
                    itemService.reportFoundItem(title, desc, finder.getUserId(), location);
                    break;

                case 2:
                    // View items reported by this finder
                    System.out.println("\n--- Your Reported Items ---");
                    ArrayList<Item> myItems = itemService.getItemsByFinder(finder.getUserId());
                    if (myItems.isEmpty()) {
                        System.out.println("You haven't reported any items yet.");
                    } else {
                        for (Item item : myItems) {
                            System.out.println(item);
                        }
                    }
                    break;

                case 3:
                    // View claims on this finder's items
                    System.out.println("\n--- Claims on Your Items ---");
                    ArrayList<Claim> claims = claimService.getClaimsForFinder(finder.getUserId());
                    if (claims.isEmpty()) {
                        System.out.println("No claims found for your items.");
                    } else {
                        for (Claim c : claims) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 4:
                    // Approve a claim
                    String approveId = input.readString("Enter Claim ID to approve: ");
                    claimService.approveClaim(approveId);
                    break;

                case 5:
                    // Reject a claim
                    String rejectId = input.readString("Enter Claim ID to reject: ");
                    claimService.rejectClaim(rejectId);
                    break;

                case 6:
                    // Start handover for an approved claim
                    String claimId4ho = input.readString("Enter Claim ID to start handover: ");
                    String hoLocation = input.readString("Handover location: ");
                    String hoRemarks  = input.readOptionalString("Remarks (optional): ");
                    try {
                        Handover ho = handoverService.startHandover(claimId4ho, hoLocation, hoRemarks);
                        System.out.println("Handover created: " + ho.getHandoverId());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 7:
                    // Confirm physical handover
                    String hoId = input.readString("Enter Handover ID to confirm: ");
                    try {
                        handoverService.confirmHandover(hoId, finder.getUserId());
                        System.out.println("Handover confirmed successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 8:
                    // View handovers involving this finder
                    System.out.println("\n--- Your Handovers ---");
                    ArrayList<Handover> handovers = handoverService.getHandoversByUser(finder.getUserId());
                    if (handovers.isEmpty()) {
                        System.out.println("No handovers found.");
                    } else {
                        for (Handover h : handovers) {
                            System.out.println(h);
                        }
                    }
                    break;

                case 9:
                    System.out.println("Logging out... Goodbye, " + finder.getName() + "!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
