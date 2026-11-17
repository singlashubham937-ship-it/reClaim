package test;

import model.Claim;
import model.ClaimStatus;
import model.OwnershipProof;
import service.Searchable;

/**
 * Temporary test class to thoroughly test Claim, OwnershipProof, ClaimStatus, and Searchable.
 */
public class ClaimModelTest {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   reClaim - Lost and Found Management System    ");
        System.out.println("   MEMBER 2: SEARCH & CLAIM MANAGEMENT TESTS     ");
        System.out.println("=================================================\n");

        int testsPassed = 0;
        int totalTests = 0;

        // -----------------------------------------------------------------
        // TEST 1: Create Ownership Proof & Validation (Complete vs Incomplete)
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 1] OwnershipProof Creation & Validation");
        OwnershipProof validProof = new OwnershipProof(
                101, 501, 202401,
                "Midnight Blue",
                "Dell",
                "Dell Inspiron 15 with GitHub sticker near the trackpad",
                "Original purchase receipt #CS-8921 from Campus Tech Store"
        );

        OwnershipProof incompleteProof = new OwnershipProof(
                "", "", "", ""
        );

        boolean validResult = validProof.validateProof();
        boolean incompleteResult = incompleteProof.validateProof();

        if (validResult && !incompleteResult) {
            System.out.println("  PASS: Valid proof accepted, incomplete proof rejected.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Proof validation logic failed.");
        }

        // -----------------------------------------------------------------
        // TEST 2: Composition - Claim containing OwnershipProof
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 2] Claim Creation with Composition (Claim has OwnershipProof)");
        Claim claim1 = new Claim(1, 501, 202401, validProof);

        if (claim1.getOwnershipProof() != null 
                && claim1.getOwnershipProof().getBrand().equals("Dell")
                && claim1.getClaimStatus() == ClaimStatus.PENDING) {
            System.out.println("  PASS: Claim successfully created in PENDING status with embedded OwnershipProof.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim creation or initial status incorrect.");
        }

        // -----------------------------------------------------------------
        // TEST 3: Null OwnershipProof rejection (Business Rule #5)
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 3] Rejection of Null OwnershipProof");
        try {
            new Claim(2, 502, 202402, null);
            System.err.println("  FAIL: Allowed creation of claim with null proof.");
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS: Correctly prevented null ownership proof: " + e.getMessage());
            testsPassed++;
        }

        // -----------------------------------------------------------------
        // TEST 4: Approve a PENDING Claim
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 4] Approve a PENDING Claim");
        claim1.approveClaim("Serial number and sticker matched with recovered item in security office.");
        if (claim1.getClaimStatus() == ClaimStatus.APPROVED) {
            System.out.println("  PASS: Claim status updated to APPROVED with remarks.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim status not APPROVED.");
        }

        // -----------------------------------------------------------------
        // TEST 5: Complete an APPROVED Claim
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 5] Complete an APPROVED Claim");
        claim1.completeClaim();
        if (claim1.getClaimStatus() == ClaimStatus.COMPLETED) {
            System.out.println("  PASS: Approved claim successfully transitioned to COMPLETED.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim status not COMPLETED.");
        }

        // -----------------------------------------------------------------
        // TEST 6: Reject another PENDING Claim
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 6] Reject another PENDING Claim");
        OwnershipProof proof2 = new OwnershipProof(
                102, 502, 202402,
                "Matte Black",
                "Casio",
                "Casio fx-991EX Scientific Calculator with initials 'BT' on back cover",
                "Belongs to Student BT (Roll #202402)"
        );
        Claim claim2 = new Claim(2, 502, 202402, proof2);
        claim2.rejectClaim("Color does not match the recovered item (found item is Silver).");

        if (claim2.getClaimStatus() == ClaimStatus.REJECTED) {
            System.out.println("  PASS: Claim status successfully set to REJECTED.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim status not REJECTED.");
        }

        // -----------------------------------------------------------------
        // TEST 7: Attempt to approve an already REJECTED claim (Invalid transition)
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 7] Prevent Approving an Already REJECTED Claim");
        try {
            claim2.approveClaim("Trying to approve after rejection");
            System.err.println("  FAIL: Invalid transition was allowed.");
        } catch (IllegalStateException e) {
            System.out.println("  PASS: Correctly caught invalid transition: " + e.getMessage());
            testsPassed++;
        }

        // -----------------------------------------------------------------
        // TEST 8: Attempt to complete a PENDING claim (Invalid transition)
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 8] Prevent Completing a PENDING Claim");
        OwnershipProof proof3 = new OwnershipProof(
                "Silver", "Apple", "MacBook Air M2 with stickers", "Serial #C02XY123"
        );
        Claim claim3 = new Claim(3, 503, 202403, proof3);
        try {
            claim3.completeClaim();
            System.err.println("  FAIL: Allowed completing a PENDING claim.");
        } catch (IllegalStateException e) {
            System.out.println("  PASS: Correctly prevented completion of PENDING claim: " + e.getMessage());
            testsPassed++;
        }

        // -----------------------------------------------------------------
        // TEST 9: toString() and displayClaimDetails() functionality
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 9] Verify toString() and displayClaimDetails()");
        String toStringOutput = claim1.toString();
        if (toStringOutput.contains("claimId=1") && toStringOutput.contains("COMPLETED")) {
            System.out.println("  PASS: toString() contains expected representation.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: toString() output is missing fields.");
        }

        System.out.println("\n--- Display Sample Claim Output ---");
        claim1.displayClaimDetails();

        // -----------------------------------------------------------------
        // TEST 10: Searchable Interface Check
        // -----------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 10] Searchable Interface implementation check");
        Searchable dummySearchable = new Searchable() {
            @Override
            public boolean searchByName(String name) {
                return name != null && name.equalsIgnoreCase("Dell");
            }
        };
        if (dummySearchable.searchByName("Dell") && !dummySearchable.searchByName("HP")) {
            System.out.println("  PASS: Searchable interface functions as expected.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Searchable interface test failed.");
        }

        // Summary
        System.out.println("\n=================================================");
        System.out.println("TEST SUMMARY: " + testsPassed + "/" + totalTests + " tests passed.");
        System.out.println("=================================================");

        if (testsPassed != totalTests) {
            System.exit(1);
        }
    }
}
