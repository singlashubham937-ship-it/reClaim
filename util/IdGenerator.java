package util;

/**
 * IdGenerator generates unique IDs for every record type in the reClaim system.
 *
 * Uses simple counter-based ID generation to keep things beginner-friendly.
 * Each record type (User, Item, Claim, Handover, LostReport, AuditLog) has its
 * own counter, so IDs never collide across types.
 *
 * Example output:
 *   USR-1, USR-2, ...
 *   ITM-1, ITM-2, ...
 *   CLM-1, CLM-2, ...
 *   HO-1,  HO-2,  ...
 *   LR-1,  LR-2,  ...
 *   LOG-1, LOG-2, ...
 *
 * OOP concept demonstrated: Encapsulation, static utility methods
 */
public class IdGenerator {

    // Separate counters for each entity type
    private static int userCounter     = 1;
    private static int itemCounter     = 1;
    private static int claimCounter    = 1;
    private static int handoverCounter = 1;
    private static int lostReportCounter = 1;
    private static int auditLogCounter = 1;

    // Private constructor – this class should never be instantiated
    private IdGenerator() {}

    /** Generates the next unique User ID, e.g. USR-1 */
    public static String nextUserId() {
        return "USR-" + userCounter++;
    }

    /** Generates the next unique Item ID, e.g. ITM-1 */
    public static String nextItemId() {
        return "ITM-" + itemCounter++;
    }

    /** Generates the next unique Claim ID, e.g. CLM-1 */
    public static String nextClaimId() {
        return "CLM-" + claimCounter++;
    }

    /** Generates the next unique Handover ID, e.g. HO-1 */
    public static String nextHandoverId() {
        return "HO-" + handoverCounter++;
    }

    /** Generates the next unique LostReport ID, e.g. LR-1 */
    public static String nextLostReportId() {
        return "LR-" + lostReportCounter++;
    }

    /** Generates the next unique AuditLog ID, e.g. LOG-1 */
    public static String nextAuditLogId() {
        return "LOG-" + auditLogCounter++;
    }

    /**
     * Resets all counters – useful only for testing.
     * Do NOT call this in the real application.
     */
    public static void resetAll() {
        userCounter     = 1;
        itemCounter     = 1;
        claimCounter    = 1;
        handoverCounter = 1;
        lostReportCounter = 1;
        auditLogCounter = 1;
    }
}
