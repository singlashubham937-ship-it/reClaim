package model;

/**
 * TEMPORARY STUB for Member 1/2's Claim class (for local compilation/testing).
 */
public class Claim {
    private String claimId;
    private String itemId;
    private String claimantId;
    private ClaimStatus status;

    public Claim(String claimId, String itemId, String claimantId, ClaimStatus status) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.status = status;
    }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getClaimantId() { return claimantId; }
    public void setClaimantId(String claimantId) { this.claimantId = claimantId; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }
}
