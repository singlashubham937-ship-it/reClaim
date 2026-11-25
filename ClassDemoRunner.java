import model.*;
import repository.*;
import service.*;
import util.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * ClassDemoRunner.java
 *
 * Terminal demonstration runner for the professor and viva panel.
 * Demonstrates:
 * 1. Object Creation & Parameterized Constructors
 * 2. Encapsulation (Private fields with Getters & Setters)
 * 3. Inheritance (Admin extends User)
 * 4. Polymorphism & Method Overriding (User reference pointing to Admin, toString() override)
 * 5. Enums (UserRole, ItemStatus, ClaimStatus)
 * 6. Repository Pattern & ArrayList Collections
 * 7. Service Layer & Business Validation
 * 8. End-to-End Workflow: Report -> Claim -> Approve -> Handover -> Confirm (CLAIMED)
 * 9. Application Statistics Calculation
 * 10. Utilities (IdGenerator, DateTimeUtil, FileStorageManager)
 */
public class ClassDemoRunner {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("               reClaim SYSTEM - CLASS & OOP DEMONSTRATION RUNNER                ");
        System.out.println("================================================================================\n");

        // -----------------------------------------------------------------------------
        // DEMO 1: ENCAPSULATION, CONSTRUCTORS, GETTERS & SETTERS
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 1: Encapsulation & Constructor initialization");
        User student = new User("USR-101", "Aarav Sharma", "aarav@univ.edu", "pass123", UserRole.STUDENT);
        System.out.println("Created User object with private fields accessed via getters:");
        System.out.println("  ID   : " + student.getUserId());
        System.out.println("  Name : " + student.getName());
        System.out.println("  Role : " + student.getRole());
        System.out.println("  Email: " + student.getEmail());
        System.out.println("Encapsulation proven: fields cannot be mutated directly without setters.\n");

        // -----------------------------------------------------------------------------
        // DEMO 2: INHERITANCE, METHOD OVERRIDING & POLYMORPHISM
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 2: Inheritance & Polymorphism (Admin extends User)");
        Admin admin = new Admin("ADM-999", "Dean Office", "admin@univ.edu", "rootAdmin");
        // Polymorphic reference: User reference holding an Admin object
        User polyUser = admin;
        System.out.println("Admin instance stored in a User variable:");
        System.out.println("  polyUser.getRole()     -> " + polyUser.getRole() + " (Always ADMIN)");
        System.out.println("  polyUser.isAdmin()     -> " + polyUser.isAdmin());
        System.out.println("  polyUser.toString()    -> " + polyUser.toString() + " (Overridden toString invoked dynamically)\n");

        // -----------------------------------------------------------------------------
        // DEMO 3: ENUMS (Type Safety)
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 3: Enum values (Restricted Type States)");
        System.out.print("  ItemStatus  : ");
        for (ItemStatus is : ItemStatus.values()) System.out.print(is + " ");
        System.out.print("\n  ClaimStatus : ");
        for (ClaimStatus cs : ClaimStatus.values()) System.out.print(cs + " ");
        System.out.print("\n  UserRole    : ");
        for (UserRole ur : UserRole.values()) System.out.print(ur + " ");
        System.out.println("\n");

        // -----------------------------------------------------------------------------
        // DEMO 4: REPOSITORIES & ARRAYLIST COLLECTIONS
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 4: Repositories (In-memory storage with ArrayList)");
        UserRepository userRepo = new UserRepository();
        ItemRepository itemRepo = new ItemRepository();
        ClaimRepository claimRepo = new ClaimRepository();
        HandoverRepository handoverRepo = new HandoverRepository();
        LostReportRepository lostRepo = new LostReportRepository();

        userRepo.addUser(student);
        User finder = new User("USR-102", "Priya Verma", "priya@univ.edu", "pass456", UserRole.FINDER);
        userRepo.addUser(finder);
        userRepo.addUser(admin); // Polymorphic insertion: Admin in UserRepository

        System.out.println("  Total users in repository: " + userRepo.getTotalUserCount());
        System.out.println("  Lookup by ID 'USR-101': " + userRepo.getUserById("USR-101").getName());
        System.out.println("  Lookup by Email 'admin@univ.edu': " + userRepo.getUserByEmail("admin@univ.edu").getRole() + "\n");

        // -----------------------------------------------------------------------------
        // DEMO 5: SERVICES & END-TO-END BUSINESS WORKFLOW
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 5: End-to-End Handover & Claim Processing Workflow");
        ItemService itemService = new ItemService(itemRepo);
        ClaimService claimService = new ClaimService(claimRepo, itemRepo);
        HandoverService handoverService = new HandoverService(handoverRepo, itemRepo, claimRepo);
        LostReportService lostReportService = new LostReportService(lostRepo);
        AdminService adminService = new AdminService(userRepo, itemRepo, lostRepo, claimRepo, handoverRepo);

        // Step 1: Student reports lost item
        LostReport lr = lostReportService.reportLostItem(student.getUserId(), "Blue Water Bottle", "Stainless steel Milton bottle", "Library 2nd Floor");
        System.out.println("  [1] Student submitted lost report: " + lr.getReportId());

        // Step 2: Finder reports found item
        Item foundItem = itemService.reportFoundItem("Blue Stainless Steel Bottle", "Found near study desks", finder.getUserId(), "Library 2nd Floor");
        System.out.println("  [2] Finder reported item: " + foundItem.getItemId() + " with status: " + foundItem.getStatus());

        // Step 3: Student submits ownership claim
        Claim claim = claimService.submitClaim(foundItem.getItemId(), student.getUserId(), "My name is etched under the base.");
        System.out.println("  [3] Ownership claim submitted: " + claim.getClaimId() + " (Status: " + claim.getStatus() + ")");

        // Step 4: Verification - Cannot handover unapproved claim
        System.out.println("  [4] Negative Testing: Attempting handover before approval...");
        try {
            handoverService.startHandover(claim.getClaimId(), "Security Desk", "Checking ID");
            System.out.println("  ERROR: Handover allowed unapproved claim!");
        } catch (IllegalStateException e) {
            System.out.println("  SUCCESS (Validation caught as expected): " + e.getMessage());
        }

        // Step 5: Finder approves the claim
        claimService.approveClaim(claim.getClaimId());
        System.out.println("  [5] Claim approved: " + claim.getClaimId() + " -> Status: " + claim.getStatus());

        // Step 6: Finder starts the handover
        Handover ho = handoverService.startHandover(claim.getClaimId(), "Main Gate Security Room", "Student presented ID card");
        System.out.println("  [6] Handover initiated: " + ho.getHandoverId() + " (Confirmed = " + ho.isConfirmed() + ")");

        // Step 7: Confirm physical handover and verify status transitions to CLAIMED
        handoverService.confirmHandover(ho.getHandoverId(), finder.getUserId());
        System.out.println("  [7] Handover confirmed! Physical exchange complete.");
        System.out.println("      Item status now: " + foundItem.getStatus());
        System.out.println("      Handover confirmed flag: " + ho.isConfirmed());
        System.out.println("      Claimed item stays in history (total items in repo = " + itemRepo.getAllItems().size() + ")");
        System.out.println("      Active items (unclaimed only) count = " + itemRepo.getActiveItems().size() + "\n");

        // -----------------------------------------------------------------------------
        // DEMO 6: AUDIT LOG VERIFICATION
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 6: Audit Log Trail");
        ArrayList<AuditLog> logs = handoverService.getAuditLogs();
        for (AuditLog log : logs) {
            System.out.println("  " + log);
        }
        System.out.println();

        // -----------------------------------------------------------------------------
        // DEMO 7: UTILITIES (IdGenerator, DateTimeUtil)
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 7: Utilities demonstration");
        System.out.println("  Generated User ID : " + IdGenerator.nextUserId());
        System.out.println("  Generated Item ID : " + IdGenerator.nextItemId());
        System.out.println("  Today's Date      : " + DateTimeUtil.todayAsString());
        System.out.println("  Current DateTime  : " + DateTimeUtil.nowAsString() + "\n");

        // -----------------------------------------------------------------------------
        // DEMO 8: ADMIN STATISTICS
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 8: Admin Dashboard Statistics Calculation");
        adminService.printStatistics();

        // -----------------------------------------------------------------------------
        // DEMO 9: FILE PERSISTENCE DEMO
        // -----------------------------------------------------------------------------
        System.out.println(">>> DEMO 9: File Persistence (Save & Reload)");
        FileStorageManager storage = new FileStorageManager(userRepo, itemRepo, claimRepo, handoverRepo, lostRepo);
        storage.saveAll();

        // Create fresh empty repositories to test loading back from files
        UserRepository freshUserRepo = new UserRepository();
        ItemRepository freshItemRepo = new ItemRepository();
        ClaimRepository freshClaimRepo = new ClaimRepository();
        HandoverRepository freshHandoverRepo = new HandoverRepository();
        LostReportRepository freshLostRepo = new LostReportRepository();

        FileStorageManager reloadStorage = new FileStorageManager(freshUserRepo, freshItemRepo, freshClaimRepo, freshHandoverRepo, freshLostRepo);
        reloadStorage.loadAll();

        System.out.println("  Reloaded user count    : " + freshUserRepo.getTotalUserCount());
        System.out.println("  Reloaded item count    : " + freshItemRepo.getAllItems().size());
        System.out.println("  Reloaded claim count   : " + freshClaimRepo.getAllClaims().size());
        System.out.println("  Reloaded handover count: " + freshHandoverRepo.getAllHandovers().size());
        System.out.println("  Reloaded lost reports  : " + freshLostRepo.getAllLostReports().size());

        System.out.println("\n================================================================================");
        System.out.println("               ALL DEMONSTRATION TESTS COMPLETED SUCCESSFULLY!                  ");
        System.out.println("================================================================================");
    }
}
