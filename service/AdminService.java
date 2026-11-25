package service;

import model.*;
import repository.*;
import java.util.ArrayList;

/**
 * AdminService provides all admin-only operations for the reClaim system.
 *
 * It REUSES the existing repositories – no new repositories are created.
 * The admin can view lists, search records, and see application statistics.
 *
 * OOP concept demonstrated: Service layer, composition (using multiple repos)
 */
public class AdminService {

    // Existing repositories – injected through the constructor
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private LostReportRepository lostReportRepository;
    private ClaimRepository claimRepository;
    private HandoverRepository handoverRepository;

    /**
     * Constructor – receives all repositories.
     * This avoids creating duplicate repository objects.
     */
    public AdminService(UserRepository userRepository,
                        ItemRepository itemRepository,
                        LostReportRepository lostReportRepository,
                        ClaimRepository claimRepository,
                        HandoverRepository handoverRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.lostReportRepository = lostReportRepository;
        this.claimRepository = claimRepository;
        this.handoverRepository = handoverRepository;
    }

    // ================================================================
    // 1. VIEW ALL USERS
    // ================================================================

    /**
     * Returns every registered user (students, finders, admins).
     */
    public ArrayList<User> viewAllUsers() {
        return userRepository.getAllUsers();
    }

    // ================================================================
    // 2. VIEW ALL ITEMS
    // ================================================================

    /**
     * Returns every found item that has been reported.
     */
    public ArrayList<Item> viewAllItems() {
        return itemRepository.getAllItems();
    }

    // ================================================================
    // 3. VIEW ALL LOST REPORTS
    // ================================================================

    /**
     * Returns every lost report submitted by students.
     */
    public ArrayList<LostReport> viewAllLostReports() {
        return lostReportRepository.getAllLostReports();
    }

    // ================================================================
    // 4. VIEW ALL CLAIMS
    // ================================================================

    /**
     * Returns every claim that has been submitted.
     */
    public ArrayList<Claim> viewAllClaims() {
        return claimRepository.getAllClaims();
    }

    // ================================================================
    // 5. VIEW ALL HANDOVERS
    // ================================================================

    /**
     * Returns every handover record (confirmed or pending).
     */
    public ArrayList<Handover> viewAllHandovers() {
        return handoverRepository.getAllHandovers();
    }

    // ================================================================
    // 6. SEARCH USER BY ID
    // ================================================================

    /**
     * Finds and returns a single user by their ID.
     * Returns null if no user is found with that ID.
     */
    public User searchUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    // ================================================================
    // 7. SEARCH ITEM BY ID
    // ================================================================

    /**
     * Finds and returns a single item by its ID.
     * Returns null if no item is found with that ID.
     */
    public Item searchItemById(String itemId) {
        return itemRepository.getItemById(itemId);
    }

    // ================================================================
    // 8. APPLICATION STATISTICS
    // ================================================================

    /**
     * Calculates and prints live statistics by counting records in repos.
     * No separate counters are maintained – the counts are always accurate.
     */
    public void printStatistics() {
        // Total users
        int totalUsers = userRepository.getAllUsers().size();

        // Users by role
        int totalStudents = userRepository.getUsersByRole(UserRole.STUDENT).size();
        int totalFinders  = userRepository.getUsersByRole(UserRole.FINDER).size();
        int totalAdmins   = userRepository.getUsersByRole(UserRole.ADMIN).size();

        // Items
        ArrayList<Item> allItems = itemRepository.getAllItems();
        int totalItems    = allItems.size();
        int foundItems    = 0;
        int claimedItems  = 0;
        for (Item item : allItems) {
            if (item.getStatus() == ItemStatus.FOUND) {
                foundItems++;
            } else if (item.getStatus() == ItemStatus.CLAIMED) {
                claimedItems++;
            }
        }

        // Claims
        ArrayList<Claim> allClaims = claimRepository.getAllClaims();
        int totalClaims    = allClaims.size();
        int pendingClaims  = 0;
        int approvedClaims = 0;
        int rejectedClaims = 0;
        for (Claim c : allClaims) {
            if (c.getStatus() == ClaimStatus.PENDING)   pendingClaims++;
            if (c.getStatus() == ClaimStatus.APPROVED)  approvedClaims++;
            if (c.getStatus() == ClaimStatus.REJECTED)  rejectedClaims++;
        }

        // Handovers
        ArrayList<Handover> allHandovers = handoverRepository.getAllHandovers();
        int totalHandovers     = allHandovers.size();
        int confirmedHandovers = 0;
        for (Handover h : allHandovers) {
            if (h.isConfirmed()) confirmedHandovers++;
        }

        // Lost reports
        int totalLostReports = lostReportRepository.getAllLostReports().size();

        // Print the statistics table
        System.out.println("\n======== reClaim Application Statistics ========");
        System.out.println("--- Users ---");
        System.out.println("  Total Users    : " + totalUsers);
        System.out.println("  Students       : " + totalStudents);
        System.out.println("  Finders        : " + totalFinders);
        System.out.println("  Admins         : " + totalAdmins);
        System.out.println("--- Items ---");
        System.out.println("  Total Items    : " + totalItems);
        System.out.println("  Found (active) : " + foundItems);
        System.out.println("  Claimed        : " + claimedItems);
        System.out.println("--- Claims ---");
        System.out.println("  Total Claims   : " + totalClaims);
        System.out.println("  Pending        : " + pendingClaims);
        System.out.println("  Approved       : " + approvedClaims);
        System.out.println("  Rejected       : " + rejectedClaims);
        System.out.println("--- Handovers ---");
        System.out.println("  Total Handovers    : " + totalHandovers);
        System.out.println("  Confirmed          : " + confirmedHandovers);
        System.out.println("--- Lost Reports ---");
        System.out.println("  Total Lost Reports : " + totalLostReports);
        System.out.println("================================================\n");
    }
}
