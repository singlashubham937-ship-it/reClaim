import model.AuditLog;
import model.Claim;
import model.ClaimStatus;
import model.Handover;
import model.Item;
import model.ItemStatus;
import repository.ClaimRepository;
import repository.HandoverRepository;
import repository.ItemRepository;
import service.HandoverService;

import java.util.ArrayList;

/**
 * Tests all 5 required scenarios for the handover workflow.
 */
public class TestHandoverWorkflow {
    public static void main(String[] args) {
        ItemRepository itemRepo = new ItemRepository();
        ClaimRepository claimRepo = new ClaimRepository();
        HandoverRepository handoverRepo = new HandoverRepository();
        HandoverService handoverService = new HandoverService(handoverRepo, itemRepo, claimRepo);

        // Setup sample items and claims
        Item laptop = new Item("IT-101", "Dell Laptop", "Black laptop found in Lab 2", "U-FINDER-1", ItemStatus.FOUND);
        Item wallet = new Item("IT-102", "Brown Wallet", "Leather wallet found near gate", "U-FINDER-2", ItemStatus.FOUND);
        itemRepo.addItem(laptop);
        itemRepo.addItem(wallet);

        Claim approvedClaim = new Claim("CL-201", "IT-101", "U-OWNER-1", ClaimStatus.APPROVED);
        Claim pendingClaim = new Claim("CL-202", "IT-102", "U-OWNER-2", ClaimStatus.PENDING);
        claimRepo.addClaim(approvedClaim);
        claimRepo.addClaim(pendingClaim);

        System.out.println("=== 1. Successful handover ===");
        Handover ho1 = handoverService.startHandover("CL-201", "Security Desk", "Verified student ID");
        System.out.println("Started: " + ho1);
        boolean confirmed = handoverService.confirmHandover(ho1.getHandoverId(), "U-ADMIN-1");
        System.out.println("Confirmed: " + confirmed);
        System.out.println("Updated Handover: " + handoverRepo.getHandoverById(ho1.getHandoverId()));

        System.out.println("\n=== 2. Handover with an invalid claim ID ===");
        try {
            handoverService.startHandover("CL-999", "Main Gate", "Invalid test");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== 3. Handover with a claim that is not approved ===");
        try {
            handoverService.startHandover("CL-202", "Library", "Pending claim test");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== 4. Attempting to hand over an already claimed item ===");
        try {
            handoverService.startHandover("CL-201", "Security Desk", "Duplicate handover test");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== 5. Checking that the item status changes to CLAIMED ===");
        System.out.println("Laptop status: " + laptop.getStatus());
        System.out.println("Active items (should exclude IT-101): " + itemRepo.getActiveItems());
        System.out.println("All items (history preserved): " + itemRepo.getAllItems());

        System.out.println("\n=== Audit Logs ===");
        ArrayList<AuditLog> logs = handoverService.getAuditLogs();
        for (AuditLog log : logs) {
            System.out.println(log);
        }
    }
}
