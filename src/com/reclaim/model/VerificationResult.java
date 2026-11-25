package com.reclaim.model;

import java.time.LocalDateTime;

/**
 * Encapsulates the evaluation outcome of an ownership proof verification check.
 */
public class VerificationResult {
    private final int claimId;
    private final int itemId;
    private final int verifierId;
    private final boolean verified;
    private final int matchConfidence; // percentage 0-100
    private final String remarks;
    private final LocalDateTime verificationTime;

    public VerificationResult(int claimId, int itemId, int verifierId, boolean verified, int matchConfidence, String remarks) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.verifierId = verifierId;
        this.verified = verified;
        this.matchConfidence = matchConfidence;
        this.remarks = remarks;
        this.verificationTime = LocalDateTime.now();
    }

    public int getClaimId() {
        return claimId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getVerifierId() {
        return verifierId;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getMatchConfidence() {
        return matchConfidence;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }

    public void displayResult() {
        System.out.println("================================================================================");
        System.out.println("                     OWNERSHIP VERIFICATION REPORT                              ");
        System.out.println("================================================================================");
        System.out.println("Claim ID         : " + claimId);
        System.out.println("Item ID          : " + itemId);
        System.out.println("Verified By (ID) : " + verifierId);
        System.out.println("Verification Date: " + verificationTime);
        System.out.println("Outcome          : " + (verified ? "VERIFIED (MATCH CONFIRMED)" : "UNVERIFIED (INSUFFICIENT MATCH)"));
        System.out.println("Match Confidence : " + matchConfidence + "%");
        System.out.println("Remarks          : " + remarks);
        System.out.println("================================================================================");
    }

    @Override
    public String toString() {
        return "VerificationResult [claimId=" + claimId + ", verified=" + verified 
                + ", confidence=" + matchConfidence + "%, remarks=" + remarks + "]";
    }
}
