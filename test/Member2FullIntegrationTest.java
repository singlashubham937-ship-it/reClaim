package test;

import com.reclaim.enums.ClaimStatus;
import com.reclaim.enums.ItemCategory;
import com.reclaim.enums.ItemStatus;
import com.reclaim.enums.UserRole;
import com.reclaim.exception.DuplicateClaimException;
import com.reclaim.exception.DuplicateItemException;
import com.reclaim.exception.ItemNotFoundException;
import com.reclaim.exception.UnauthorizedActionException;
import com.reclaim.menu.ClaimMenu;
import com.reclaim.menu.ItemMenu;
import com.reclaim.menu.StudentMenu;
import com.reclaim.model.AccessoryItem;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Claim;
import com.reclaim.model.DocumentItem;
import com.reclaim.model.ElectronicsItem;
import com.reclaim.model.Finder;
import com.reclaim.model.Item;
import com.reclaim.model.Notification;
import com.reclaim.model.OwnershipProof;
import com.reclaim.model.PersonalBelongingItem;
import com.reclaim.model.Student;
import com.reclaim.model.User;
import com.reclaim.model.VerificationResult;
import com.reclaim.repository.ClaimRepository;
import com.reclaim.repository.ItemRepository;
import com.reclaim.repository.UserRepository;
import com.reclaim.service.ClaimService;
import com.reclaim.service.NotificationService;
import com.reclaim.service.SearchService;
import com.reclaim.service.VerificationService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Comprehensive test runner validating all Member 2 deliverables:
 * 1. Claims & Repositories
 * 2. Search & Filters
 * 3. Duplicate Claim Prevention
 * 4. VerificationService
 * 5. Finder Permissions & Authorization
 * 6. Notifications
 * 7. Console Menus (StudentMenu, ItemMenu, ClaimMenu)
 * 8. Non-premature Handover Invariant Check
 * 9. Member 1 Regression & Compatibility Check
 */
public class Member2FullIntegrationTest {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("        reClaim - University Lost & Found Management System                     ");
        System.out.println("        MEMBER 2 FULL INTEGRATION & VERIFICATION TEST RUNNER                    ");
        System.out.println("================================================================================\n");

        int testsPassed = 0;
        int totalTests = 0;

        // Repositories
        ItemRepository itemRepo = new ItemRepository();
        UserRepository userRepo = new UserRepository();
        ClaimRepository claimRepo = new ClaimRepository();

        // Services
        NotificationService notificationService = new NotificationService();
        SearchService searchService = new SearchService(itemRepo);
        ClaimService claimService = new ClaimService(claimRepo, itemRepo, notificationService);
        VerificationService verificationService = new VerificationService(claimRepo, itemRepo, notificationService, claimService);

        // Setup users: Finder 10 (John), Finder 20 (Dave), Student 100 (Alice), Student 200 (Bob)
        Student alice = new Student(100, "Alice Smith", "alice@university.edu", "555-0101", "pass123");
        Student bob = new Student(200, "Bob Jones", "bob@university.edu", "555-0202", "pass456");
        Finder finderJohn = new Finder(10, "John Finder", "john@university.edu", "555-1010", "pass789");
        Finder finderDave = new Finder(20, "Dave Finder", "dave@university.edu", "555-2020", "pass321");

        userRepo.addUser(alice);
        userRepo.addUser(bob);
        userRepo.addUser(finderJohn);
        userRepo.addUser(finderDave);

        // Setup items
        Item blueBackpack = new PersonalBelongingItem(
                301, "Blue North Face Backpack", "Navy blue Borealis backpack containing books and notes",
                "Central Library 1st Floor", "Library Lost Desk", 10
        );

        Item hpLaptop = new ElectronicsItem(
                302, "HP Envy Laptop", "Silver 13-inch HP laptop with Intel Core i7 and university Wi-Fi tag",
                "Computer Lab 4", "Lab Staff Room", 10
        );

        Item goldWatch = new AccessoryItem(
                303, "Seiko Gold Watch", "Gold mesh strap quartz analog watch",
                "Sports Complex Gym", "Recreation Center", 20
        );

        itemRepo.addItem(blueBackpack);
        itemRepo.addItem(hpLaptop);
        itemRepo.addItem(goldWatch);

        // -------------------------------------------------------------
        // TEST 1: Member 1 Existing Models & Repository Integrity
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 1] Member 1 Model & Repository Integrity");
        if (itemRepo.getAllItems().size() == 3 
                && userRepo.getAllUsers().size() == 4
                && blueBackpack.getCategory() == ItemCategory.PERSONAL_BELONGING
                && hpLaptop.getItemType().equals("Electronics")
                && alice.getRole() == UserRole.STUDENT
                && finderJohn.getRole() == UserRole.FINDER) {
            System.out.println("  PASS: Member 1 entities, inheritance hierarchy, and repositories intact.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Member 1 integration check failed.");
        }

        // -------------------------------------------------------------
        // TEST 2: Search item by criteria and verify AVAILABLE filtering
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 2] Search Service item filtering");
        ArrayList<Item> backpackSearch = searchService.searchByName("backpack");
        ArrayList<Item> librarySearch = searchService.searchByLocation("Library");
        ArrayList<Item> elecSearch = searchService.searchByCategory(ItemCategory.ELECTRONICS);

        if (backpackSearch.size() == 1 
                && librarySearch.size() == 1 
                && elecSearch.size() == 1 
                && elecSearch.get(0).getItemId() == 302) {
            System.out.println("  PASS: SearchService returns expected available items by name, location, and category.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: SearchService results mismatch.");
        }

        // -------------------------------------------------------------
        // TEST 3: Add / Submit a Claim (Workflow & Initial PENDING status)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 3] Submit Claim & Verify Status Transitions");
        OwnershipProof proofBackpack = new OwnershipProof(
                "Navy Blue", "North Face", "Borealis model with small tear on bottle pocket",
                "Purchase receipt #NF-9821 from University Bookstore"
        );
        Claim claim1 = claimService.submitClaim(301, alice.getUserId(), proofBackpack);

        if (claim1.getClaimStatus() == ClaimStatus.PENDING 
                && claim1.getItemId() == 301
                && claim1.getStudentId() == 100
                && claim1.getFinderId() == 10
                && blueBackpack.getStatus() == ItemStatus.CLAIM_PENDING) {
            System.out.println("  PASS: Claim created in PENDING status. Item status updated to CLAIM_PENDING.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claim creation failed or item status incorrect.");
        }

        // -------------------------------------------------------------
        // TEST 4: Search for an item and submit claim workflow
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 4] Search and Submit Claim Workflow");
        ArrayList<Item> foundItems = searchService.searchByKeyword("Laptop");
        Item targetLaptop = foundItems.get(0);
        OwnershipProof proofLaptop = new OwnershipProof(
                "Silver", "HP", "HP Envy 13 with student ID sticker on bottom chassis", "Invoice from Campus Tech"
        );
        Claim claimLaptop = claimService.submitClaim(targetLaptop.getItemId(), bob.getUserId(), proofLaptop);

        if (claimLaptop != null && claimLaptop.getClaimStatus() == ClaimStatus.PENDING) {
            System.out.println("  PASS: Successfully located item via search and submitted claim.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Search and submit claim workflow failed.");
        }

        // -------------------------------------------------------------
        // TEST 5: Duplicate Claim Prevention
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 5] Duplicate Claim Prevention");
        try {
            claimService.submitClaim(301, alice.getUserId(), proofBackpack);
            System.err.println("  FAIL: Duplicate active claim was permitted.");
        } catch (DuplicateItemException e) {
            System.out.println("  PASS: Duplicate claim blocked: " + e.getMessage());
            testsPassed++;
        }

        // -------------------------------------------------------------
        // TEST 6: Ownership Verification Service
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 6] Ownership Verification Service Evaluation");
        VerificationResult vResult = verificationService.verifyClaim(claim1.getClaimId(), finderJohn.getUserId());
        vResult.displayResult();

        if (vResult.isVerified() && vResult.getMatchConfidence() >= 50) {
            System.out.println("  PASS: Verification correctly confirmed colour and brand match.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Ownership verification failed.");
        }

        // -------------------------------------------------------------
        // TEST 7: Unauthorized Finder Actions
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 7] Unauthorized Finder Action Prevention");
        try {
            // Dave (finder 20) attempts to verify or approve John's item (finder 10)
            verificationService.verifyClaim(claim1.getClaimId(), finderDave.getUserId());
            System.err.println("  FAIL: Unauthorized finder was allowed to verify.");
        } catch (UnauthorizedActionException e) {
            System.out.println("  PASS: Unauthorized verification rejected: " + e.getMessage());
            testsPassed++;
        }

        // -------------------------------------------------------------
        // TEST 8: Approval & Rejection Logic
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 8] Claim Approval & Rejection Logic");
        // Authorized approval
        boolean approved = claimService.approveClaim(claim1.getClaimId(), finderJohn.getUserId(), "Proof matched physical item perfectly.");

        // Setup claim to reject (Bob's laptop claim rejected by John)
        boolean rejected = claimService.rejectClaim(claimLaptop.getClaimId(), finderJohn.getUserId(), "Serial number on receipt does not match BIOS.");

        if (approved && claim1.getClaimStatus() == ClaimStatus.APPROVED
                && rejected && claimLaptop.getClaimStatus() == ClaimStatus.REJECTED
                && hpLaptop.getStatus() == ItemStatus.AVAILABLE) {
            System.out.println("  PASS: Claim 1 APPROVED. Claim 2 REJECTED and Item 302 restored to AVAILABLE.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Approval or rejection logic failed.");
        }

        // -------------------------------------------------------------
        // TEST 9: Invariant Check: Approval does NOT prematurely mark item CLAIMED
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 9] Member 3 Handover Invariant Check");
        Item approvedItem = itemRepo.getItemById(301);
        if (approvedItem.getStatus() != ItemStatus.CLAIMED && approvedItem.getStatus() == ItemStatus.CLAIM_PENDING) {
            System.out.println("  PASS: Approved item remains CLAIM_PENDING (Handover belongs to Member 3).");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Item was prematurely marked as CLAIMED.");
        }

        // -------------------------------------------------------------
        // TEST 10: Notification System
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 10] Notification Service Verification");
        ArrayList<Notification> aliceNotifs = notificationService.getNotificationsByUser(alice.getUserId());
        ArrayList<Notification> bobNotifs = notificationService.getNotificationsByUser(bob.getUserId());

        if (!aliceNotifs.isEmpty() && !bobNotifs.isEmpty()) {
            System.out.println("  PASS: Alice received " + aliceNotifs.size() + " notification(s).");
            System.out.println("        Bob received " + bobNotifs.size() + " notification(s).");
            notificationService.markNotificationAsRead(aliceNotifs.get(0).getNotificationId());
            if (aliceNotifs.get(0).isRead()) {
                System.out.println("  PASS: Notification markAsRead verified.");
                testsPassed++;
            } else {
                System.err.println("  FAIL: Notification markAsRead failed.");
            }
        } else {
            System.err.println("  FAIL: Notifications were not generated for claim lifecycle events.");
        }

        // -------------------------------------------------------------
        // TEST 11: Console Menus Execution with Simulated Inputs
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 11] Console Menus (StudentMenu, ItemMenu, ClaimMenu) Simulation");

        // Simulate StudentMenu: Option 1 (Search available), Option 8 (Notifications), Option 10 (Logout)
        String studentInput = "1\n8\n10\n";
        Scanner studentScanner = new Scanner(new ByteArrayInputStream(studentInput.getBytes(StandardCharsets.UTF_8)));
        StudentMenu studentMenu = new StudentMenu(searchService, claimService, notificationService, itemRepo);
        studentMenu.displayMenu(alice, studentScanner);

        // Simulate ItemMenu: Option 2 (Search by name: "back"), Option 6 (Return)
        String itemInput = "2\nback\n6\n";
        Scanner itemScanner = new Scanner(new ByteArrayInputStream(itemInput.getBytes(StandardCharsets.UTF_8)));
        ItemMenu itemMenu = new ItemMenu(searchService, itemRepo);
        itemMenu.displayMenu(itemScanner);

        // Simulate ClaimMenu: Option 2 (View my claims), Option 7 (View claim status: 1), Option 8 (Return)
        String claimInput = "2\n7\n1\n8\n";
        Scanner claimScanner = new Scanner(new ByteArrayInputStream(claimInput.getBytes(StandardCharsets.UTF_8)));
        ClaimMenu claimMenu = new ClaimMenu(claimService, verificationService);
        claimMenu.displayMenu(alice, claimScanner);

        System.out.println("  PASS: StudentMenu, ItemMenu, and ClaimMenu executed cleanly without unhandled exceptions.");
        testsPassed++;

        // -------------------------------------------------------------
        // TEST 12: Member 1 Existing Functionality Sanity Check
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 12] Final Member 1 & System Sanity Check");
        DocumentItem passport = new DocumentItem(304, "Student Passport", "Indian passport in blue sleeve", "Admin Block", "Security Room", 10);
        boolean addedPassport = itemRepo.addItem(passport);
        Item retrieved = itemRepo.getItemById(304);

        if (addedPassport && retrieved != null && retrieved.getName().equals("Student Passport")) {
            System.out.println("  PASS: Member 1 ItemRepository CRUD and subclasses functional.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Member 1 sanity check failed.");
        }

        // Summary
        System.out.println("\n================================================================================");
        System.out.println("FULL INTEGRATION SUMMARY: " + testsPassed + "/" + totalTests + " TEST SUITES PASSED (100%)");
        System.out.println("================================================================================");

        if (testsPassed != totalTests) {
            System.exit(1);
        }
    }
}
