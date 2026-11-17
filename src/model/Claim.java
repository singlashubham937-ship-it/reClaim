package model;

import java.time.LocalDateTime;

/**
 * Represents an item claim in the reClaim system.
 * Uses composition: Claim contains an OwnershipProof object.
 * Applies encapsulation using private fields and controlled business methods.
 */
public class Claim {
    private int claimId;
    private int itemId;
    private int studentId;
    private int finderId;
    private OwnershipProof ownershipProof;
    private ClaimStatus claimStatus;
    private LocalDateTime claimDate;
    private String verificationRemarks;

    // Default constructor
    public Claim() {
        this.claimStatus = ClaimStatus.PENDING;
        this.claimDate = LocalDateTime.now();
        this.verificationRemarks = "Pending verification";
    }

    // Constructor with core parameters
    public Claim(int claimId, int itemId, int studentId, OwnershipProof ownershipProof) {
        if (ownershipProof == null) {
            throw new IllegalArgumentException("Ownership proof cannot be null for a valid claim.");
        }
        this.claimId = claimId;
        this.itemId = itemId;
        this.studentId = studentId;
        this.finderId = 0;
        this.ownershipProof = ownershipProof;
        this.claimStatus = ClaimStatus.PENDING;
        this.claimDate = LocalDateTime.now();
        this.verificationRemarks = "Pending verification";
    }

    // Full constructor including finderId
    public Claim(int claimId, int itemId, int studentId, int finderId, OwnershipProof ownershipProof) {
        this(claimId, itemId, studentId, ownershipProof);
        this.finderId = finderId;
    }

    // Full constructor with custom date and remarks
    public Claim(int claimId, int itemId, int studentId, OwnershipProof ownershipProof,
                 ClaimStatus claimStatus, LocalDateTime claimDate, String verificationRemarks) {
        if (ownershipProof == null) {
            throw new IllegalArgumentException("Ownership proof cannot be null for a valid claim.");
        }
        this.claimId = claimId;
        this.itemId = itemId;
        this.studentId = studentId;
        this.finderId = 0;
        this.ownershipProof = ownershipProof;
        this.claimStatus = claimStatus != null ? claimStatus : ClaimStatus.PENDING;
        this.claimDate = claimDate != null ? claimDate : LocalDateTime.now();
        this.verificationRemarks = verificationRemarks != null ? verificationRemarks : "Pending verification";
    }

    // Getters and Setters
    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Alias for ownerId compatibility
    public int getOwnerId() {
        return studentId;
    }

    public void setOwnerId(int ownerId) {
        this.studentId = ownerId;
    }

    public int getFinderId() {
        return finderId;
    }

    public void setFinderId(int finderId) {
        this.finderId = finderId;
    }

    public OwnershipProof getOwnershipProof() {
        return ownershipProof;
    }

    public void setOwnershipProof(OwnershipProof ownershipProof) {
        if (ownershipProof == null) {
            throw new IllegalArgumentException("Ownership proof cannot be null.");
        }
        this.ownershipProof = ownershipProof;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public String getVerificationRemarks() {
        return verificationRemarks;
    }

    public void setVerificationRemarks(String verificationRemarks) {
        this.verificationRemarks = verificationRemarks;
    }

    /**
     * Updates the claim status following university business rules.
     * Rule: Only PENDING claims can be APPROVED or REJECTED.
     * Rule: Only APPROVED claims can be COMPLETED.
     */
    public void updateClaimStatus(ClaimStatus newStatus, String remarks) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New claim status cannot be null.");
        }

        // Only PENDING claims can be APPROVED or REJECTED
        if ((newStatus == ClaimStatus.APPROVED || newStatus == ClaimStatus.REJECTED)
                && this.claimStatus != ClaimStatus.PENDING) {
            throw new IllegalStateException("Only PENDING claims can be approved or rejected. Current status: " + this.claimStatus);
        }

        // Only APPROVED claims can be COMPLETED
        if (newStatus == ClaimStatus.COMPLETED && this.claimStatus != ClaimStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED claims can be completed. Current status: " + this.claimStatus);
        }

        this.claimStatus = newStatus;
        if (remarks != null && !remarks.trim().isEmpty()) {
            this.verificationRemarks = remarks;
        }
    }

    public void updateClaimStatus(ClaimStatus newStatus) {
        updateClaimStatus(newStatus, this.verificationRemarks);
    }

    /**
     * Approves a pending claim.
     */
    public void approveClaim(String remarks) {
        updateClaimStatus(ClaimStatus.APPROVED, remarks);
    }

    /**
     * Rejects a pending claim.
     */
    public void rejectClaim(String remarks) {
        updateClaimStatus(ClaimStatus.REJECTED, remarks);
    }

    /**
     * Marks an approved claim as completed after item handover.
     */
    public void completeClaim() {
        updateClaimStatus(ClaimStatus.COMPLETED, "Claim completed and item handed over to owner.");
    }

    /**
     * Displays all details of the claim, including its ownership proof.
     */
    public void displayClaimDetails() {
        System.out.println("========================================");
        System.out.println("             CLAIM DETAILS              ");
        System.out.println("========================================");
        System.out.println("Claim ID             : " + claimId);
        System.out.println("Item ID              : " + itemId);
        System.out.println("Student ID           : " + studentId);
        if (finderId > 0) {
            System.out.println("Finder ID            : " + finderId);
        }
        System.out.println("Claim Status         : " + claimStatus);
        System.out.println("Claim Date           : " + claimDate);
        System.out.println("Verification Remarks : " + verificationRemarks);
        System.out.println("----------------------------------------");
        if (ownershipProof != null) {
            ownershipProof.displayProof();
        } else {
            System.out.println("No ownership proof attached.");
        }
        System.out.println("========================================");
    }

    public void displayClaim() {
        displayClaimDetails();
    }

    @Override
    public String toString() {
        return "Claim [claimId=" + claimId + ", itemId=" + itemId + ", studentId=" + studentId
                + ", claimStatus=" + claimStatus + ", claimDate=" + claimDate
                + ", remarks=" + verificationRemarks + ", proof=" + ownershipProof + "]";
    }
}
