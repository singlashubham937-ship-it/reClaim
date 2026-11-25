package test;

import com.reclaim.enums.ClaimStatus;
import com.reclaim.enums.ItemCategory;
import com.reclaim.enums.ItemStatus;
import com.reclaim.exception.DuplicateClaimException;
import com.reclaim.exception.DuplicateItemException;
import com.reclaim.exception.ItemNotFoundException;
import com.reclaim.exception.UnauthorizedActionException;
import com.reclaim.model.Claim;
import com.reclaim.model.ElectronicsItem;
import com.reclaim.model.Item;
import com.reclaim.model.OwnershipProof;
import com.reclaim.model.PersonalBelongingItem;
import com.reclaim.repository.ClaimRepository;
import com.reclaim.repository.ItemRepository;
import com.reclaim.service.ClaimService;

import java.util.ArrayList;

/**
 * Unit test suite verifying ClaimRepository, ClaimService, and university business rules.
 */
public class ClaimServiceTest {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("          reClaim - University Lost and Found Management System                 ");
        System.out.println("          MEMBER 2: CLAIM SERVICE & REPOSITORY WORKFLOW TESTS                   ");
        System.out.println("================================================================================\n");

        int testsPassed = 0;
        int totalTests = 0;

        // Initialize repositories and service
        ItemRepository itemRepository = new ItemRepository();
        ClaimRepository claimRepository = new ClaimRepository();
        ClaimService claimService = new ClaimService(claimRepository, itemRepository);

        // Setup sample items
        // Item 1: Found by User 50 (Library Desk Assistant)
        Item blueBottle = new PersonalBelongingItem(
                201,
                "Hydro Flask Water Bottle",
                "Pacific Blue 32oz bottle with national parks stickers",
                "Central Library Reading Room",
                "Library Desk",
                50
        );

        // Item 2: Found by User 60 (Tech Lab Assistant)
        Item headphones = new ElectronicsItem(
                202,
                "Sony WH-1000XM4 Headphones",
                "Black wireless noise-canceling headphones in gray case",
                "Computer Lab 3",
                "Lab Assistant Office",
                60
        );

        itemRepository.addItem(blueBottle);
        itemRepository.addItem(headphones);

        // Sample student ID: 1001 (Alice), Student 1002 (Bob)
        int studentAlice = 1001;
        int studentBob = 1002;
        int finder50 = 50;
        int unauthorizedUser = 99;

        // Valid proof
        OwnershipProof proofAlice = new OwnershipProof(
                "Pacific Blue",
                "Hydro Flask",
                "32oz wide mouth with Yosemite and Yellowstone stickers on the side",
                "Registered on student account #STU1001"
        );

        // -------------------------------------------------------------
        // TEST 1: Successful claim submission
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 1] Successful Claim Submission");
        Claim claim1 = claimService.submitClaim(201, studentAlice, proofAlice);

        if (claim1 != null 
                && claim1.getClaimStatus() == ClaimStatus.PENDING
                && claim1.getItemId() == 201
                && claim1.getStudentId() == studentAlice
                && claim1.getFinderId() == finder50
                && blueBottle.getStatus() == ItemStatus.CLAIM_PENDING) {
            System.out.println("  PASS: Claim successfully submitted in PENDING status.");
            System.out.println("        Item status updated to CLAIM_PENDING.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim submission failed or state is incorrect.");
        }

        // -------------------------------------------------------------
        // TEST 2: Duplicate claim rejection (same student, same item)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 2] Duplicate Claim Rejection");
        try {
            claimService.submitClaim(201, studentAlice, proofAlice);
            System.err.println("  FAIL: Duplicate claim was allowed.");
        } catch (DuplicateItemException e) {
            System.out.println("  PASS: Duplicate claim prevented: " + e.getMessage());
            testsPassed++;
        }

        // -------------------------------------------------------------
        // TEST 3: Claim for nonexistent item
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 3] Claim for Nonexistent Item (ID 9999)");
        try {
            claimService.submitClaim(9999, studentAlice, proofAlice);
            System.err.println("  FAIL: Claim on nonexistent item was allowed.");
        } catch (ItemNotFoundException e) {
            System.out.println("  PASS: Correctly threw ItemNotFoundException: " + e.getMessage());
            testsPassed++;
        }

        // -------------------------------------------------------------
        // TEST 4: Pending claim retrieval
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 4] Pending Claim Retrieval");
        // Submit another claim on headphones by Bob
        OwnershipProof proofBob = new OwnershipProof(
                "Black",
                "Sony",
                "WH-1000XM4 with small scratch on the right ear cup",
                "Purchased from BestBuy with digital receipt"
        );
        Claim claim2 = claimService.submitClaim(202, studentBob, proofBob);

        ArrayList<Claim> pendingClaims = claimService.viewPendingClaims();
        if (pendingClaims.size() == 2 && pendingClaims.contains(claim1) && pendingClaims.contains(claim2)) {
            System.out.println("  PASS: Retrieved all 2 pending claims accurately.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Pending claim retrieval returned unexpected count: " + pendingClaims.size());
        }

        // -------------------------------------------------------------
        // TEST 5: Unauthorized approval attempt
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 5] Unauthorized Approval Attempt (User " + unauthorizedUser + " vs Finder " + finder50 + ")");
        try {
            claimService.approveClaim(claim1.getClaimId(), unauthorizedUser, "Attempted unauthorized approval");
            System.err.println("  FAIL: Unauthorized approval was allowed.");
        } catch (UnauthorizedActionException e) {
            System.out.println("  PASS: Correctly caught UnauthorizedActionException: " + e.getMessage());
            testsPassed++;
        }

        // -------------------------------------------------------------
        // TEST 6: Authorized approval & Handover responsibility verification
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 6] Authorized Approval by Finder " + finder50);
        boolean approved = claimService.approveClaim(claim1.getClaimId(), finder50, "Sticker details matched recovered item.");
        Item item201 = itemRepository.getItemById(201);

        // Verification: Item status must NOT be CLAIMED (Handover is Member 3's job)
        if (approved 
                && claim1.getClaimStatus() == ClaimStatus.APPROVED
                && item201.getStatus() != ItemStatus.CLAIMED
                && item201.getStatus() == ItemStatus.CLAIM_PENDING) {
            System.out.println("  PASS: Claim status is APPROVED.");
            System.out.println("        Item status is preserved as CLAIM_PENDING (NOT marked CLAIMED).");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Authorized approval failed or item incorrectly marked CLAIMED.");
        }

        // -------------------------------------------------------------
        // TEST 7: Rejection by authorized finder & item availability restoration
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 7] Authorized Rejection by Finder 60 on Item 202");
        boolean rejected = claimService.rejectClaim(claim2.getClaimId(), 60, "Serial number does not match receipt.");
        Item item202 = itemRepository.getItemById(202);

        if (rejected 
                && claim2.getClaimStatus() == ClaimStatus.REJECTED
                && item202.getStatus() == ItemStatus.AVAILABLE) {
            System.out.println("  PASS: Claim status is REJECTED.");
            System.out.println("        Item status restored to AVAILABLE for other claimants.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Rejection logic failed.");
        }

        // -------------------------------------------------------------
        // TEST 8: View claims by student ID (viewMyClaims)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 8] View Student Claims (viewMyClaims)");
        ArrayList<Claim> aliceClaims = claimService.viewMyClaims(studentAlice);
        if (aliceClaims.size() == 1 && aliceClaims.get(0).getClaimId() == claim1.getClaimId()) {
            System.out.println("  PASS: viewMyClaims returned Alice's claim correctly.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: viewMyClaims failed.");
        }

        // Summary
        System.out.println("\n================================================================================");
        System.out.println("TEST SUMMARY: " + testsPassed + "/" + totalTests + " test suites PASSED.");
        System.out.println("================================================================================");

        if (testsPassed != totalTests) {
            System.exit(1);
        }
    }
}
