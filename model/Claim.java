package model;

/**
 * Represents a claim submitted by a student (potential owner) for a found item.
 *
 * OOP concept demonstrated: Encapsulation
 */
public class Claim {

    private String claimId;
    private String itemId;
    private String claimantId;   // The user who submitted the claim
    private ClaimStatus status;
    private String remarks;      // Optional notes from the claimant

    /**
     * Full constructor.
     */
    public Claim(String claimId, String itemId, String claimantId, ClaimStatus status) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.status = status;
        this.remarks = "";
    }

    /**
     * Constructor with remarks.
     */
    public Claim(String claimId, String itemId, String claimantId, ClaimStatus status, String remarks) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.status = status;
        this.remarks = remarks;
    }

    // ---- Getters & Setters ----

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getClaimantId() { return claimantId; }
    public void setClaimantId(String claimantId) { this.claimantId = claimantId; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public String toString() {
        return "Claim{" +
                "claimId='" + claimId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", claimantId='" + claimantId + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
