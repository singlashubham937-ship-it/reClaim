package com.reclaim.menu;

import com.reclaim.enums.ClaimStatus;
import com.reclaim.model.Claim;
import com.reclaim.model.OwnershipProof;
import com.reclaim.model.User;
import com.reclaim.model.VerificationResult;
import com.reclaim.service.ClaimService;
import com.reclaim.service.VerificationService;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console menu dedicated to claim submission, finder reviews, ownership verification, and status lookups.
 */
public class ClaimMenu {

    private final ClaimService claimService;
    private final VerificationService verificationService;

    public ClaimMenu(ClaimService claimService, VerificationService verificationService) {
        this.claimService = claimService;
        this.verificationService = verificationService;
    }

    public void displayMenu(User loggedInUser, Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Error: User session is required to access the Claim Menu.");
            return;
        }

        int userId = loggedInUser.getUserId();
        boolean running = true;

        while (running) {
            System.out.println("\n================================================================================");
            System.out.println("                        CLAIM MANAGEMENT MENU                                   ");
            System.out.println("                   User: " + loggedInUser.getName() + " (Role: " + loggedInUser.getRole() + ")");
            System.out.println("================================================================================");
            System.out.println(" 1. Submit a claim");
            System.out.println(" 2. View my claims");
            System.out.println(" 3. View pending claims for the logged-in finder");
            System.out.println(" 4. Verify ownership proof");
            System.out.println(" 5. Approve a claim");
            System.out.println(" 6. Reject a claim");
            System.out.println(" 7. View claim status");
            System.out.println(" 8. Return to previous menu");
            System.out.println("================================================================================");
            System.out.print("Enter choice (1-8): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleSubmitClaim(userId, scanner);
                    break;
                case "2":
                    handleViewMyClaims(userId);
                    break;
                case "3":
                    handleViewPendingClaimsForFinder(userId);
                    break;
                case "4":
                    handleVerifyOwnershipProof(userId, scanner);
                    break;
                case "5":
                    handleApproveClaim(userId, scanner);
                    break;
                case "6":
                    handleRejectClaim(userId, scanner);
                    break;
                case "7":
                    handleViewClaimStatus(scanner);
                    break;
                case "8":
                    System.out.println("Returning to previous menu...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 8.");
                    break;
            }
        }
    }

    private void handleSubmitClaim(int studentId, Scanner scanner) {
        System.out.println("\n--- Submit a Claim ---");
        System.out.print("Enter Item ID: ");
        try {
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Item Colour: ");
            String colour = scanner.nextLine().trim();
            System.out.print("Item Brand: ");
            String brand = scanner.nextLine().trim();
            System.out.print("Identifying Description: ");
            String description = scanner.nextLine().trim();
            System.out.print("Additional Ownership Details: ");
            String additional = scanner.nextLine().trim();

            OwnershipProof proof = new OwnershipProof(colour, brand, description, additional);
            Claim claim = claimService.submitClaim(itemId, studentId, proof);
            System.out.println("\nSUCCESS: Claim #" + claim.getClaimId() + " created with PENDING status.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Item ID must be an integer.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewMyClaims(int studentId) {
        ArrayList<Claim> claims = claimService.viewStudentClaims(studentId);
        if (claims.isEmpty()) {
            System.out.println("No claims submitted by you.");
            return;
        }
        System.out.println("\n--- Your Submitted Claims ---");
        for (Claim c : claims) {
            c.displayClaimDetails();
        }
    }

    private void handleViewPendingClaimsForFinder(int finderId) {
        ArrayList<Claim> allPending = claimService.viewPendingClaims();
        ArrayList<Claim> finderPending = new ArrayList<>();

        for (Claim c : allPending) {
            if (c.getFinderId() == finderId) {
                finderPending.add(c);
            }
        }

        if (finderPending.isEmpty()) {
            System.out.println("No pending claims found for your found items.");
            return;
        }

        System.out.println("\n================================================================================");
        System.out.println("             PENDING CLAIMS FOR FINDER ID " + finderId + " (" + finderPending.size() + ")             ");
        System.out.println("================================================================================");
        for (Claim c : finderPending) {
            c.displayClaimDetails();
        }
    }

    private void handleVerifyOwnershipProof(int verifierId, Scanner scanner) {
        System.out.print("Enter Claim ID to verify: ");
        try {
            int claimId = Integer.parseInt(scanner.nextLine().trim());
            if (verificationService != null) {
                VerificationResult result = verificationService.verifyClaim(claimId, verifierId);
                result.displayResult();
            } else {
                System.out.println("Verification service is unavailable.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Claim ID must be a numeric value.");
        } catch (Exception e) {
            System.out.println("Verification Error: " + e.getMessage());
        }
    }

    private void handleApproveClaim(int verifierId, Scanner scanner) {
        System.out.print("Enter Claim ID to approve: ");
        try {
            int claimId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter approval remarks: ");
            String remarks = scanner.nextLine().trim();

            boolean approved = claimService.approveClaim(claimId, verifierId, remarks);
            if (approved) {
                System.out.println("\nSUCCESS: Claim #" + claimId + " has been APPROVED.");
                System.out.println("Item remains in CLAIM_PENDING until physical handover is processed by Member 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Claim ID must be an integer.");
        } catch (Exception e) {
            System.out.println("Approval Error: " + e.getMessage());
        }
    }

    private void handleRejectClaim(int verifierId, Scanner scanner) {
        System.out.print("Enter Claim ID to reject: ");
        try {
            int claimId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter rejection remarks: ");
            String remarks = scanner.nextLine().trim();

            boolean rejected = claimService.rejectClaim(claimId, verifierId, remarks);
            if (rejected) {
                System.out.println("\nSUCCESS: Claim #" + claimId + " has been REJECTED.");
                System.out.println("Item availability restored if no other active claims exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Claim ID must be an integer.");
        } catch (Exception e) {
            System.out.println("Rejection Error: " + e.getMessage());
        }
    }

    private void handleViewClaimStatus(Scanner scanner) {
        System.out.print("Enter Claim ID: ");
        try {
            int claimId = Integer.parseInt(scanner.nextLine().trim());
            Claim claim = claimService.getClaimDetails(claimId);
            if (claim != null) {
                claim.displayClaimDetails();
            } else {
                System.out.println("No claim found with ID " + claimId + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Claim ID must be an integer.");
        }
    }
}
