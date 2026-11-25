package com.reclaim.repository;

import com.reclaim.enums.ClaimStatus;
import com.reclaim.model.Claim;

import java.util.ArrayList;

/**
 * Repository holding in-memory Claim records for the reClaim system.
 * Uses an ArrayList<Claim> and provides CRUD and query methods.
 */
public class ClaimRepository {
    private final ArrayList<Claim> claims;

    public ClaimRepository() {
        this.claims = new ArrayList<>();
    }

    /**
     * Adds a claim to the repository if not null and claimId is not duplicate.
     *
     * @param claim The claim to add.
     * @return true if added successfully, false otherwise.
     */
    public boolean addClaim(Claim claim) {
        if (claim == null) {
            return false;
        }
        if (findClaimById(claim.getClaimId()) != null) {
            return false;
        }
        return claims.add(claim);
    }

    /**
     * Returns all claims currently stored.
     */
    public ArrayList<Claim> getAllClaims() {
        return new ArrayList<>(claims);
    }

    /**
     * Finds a claim by its unique claimId.
     *
     * @param claimId The claim ID to search for.
     * @return The Claim if found, or null.
     */
    public Claim findClaimById(int claimId) {
        for (Claim claim : claims) {
            if (claim.getClaimId() == claimId) {
                return claim;
            }
        }
        return null;
    }

    /**
     * Alias for findClaimById to adhere to standard getter naming.
     */
    public Claim getClaimById(int claimId) {
        return findClaimById(claimId);
    }

    /**
     * Finds all claims submitted by a given student.
     *
     * @param studentId The ID of the student.
     * @return An ArrayList of matching claims.
     */
    public ArrayList<Claim> findClaimsByStudentId(int studentId) {
        ArrayList<Claim> result = new ArrayList<>();
        for (Claim claim : claims) {
            if (claim.getStudentId() == studentId) {
                result.add(claim);
            }
        }
        return result;
    }

    /**
     * Alias for findClaimsByStudentId.
     */
    public ArrayList<Claim> getClaimsByStudentId(int studentId) {
        return findClaimsByStudentId(studentId);
    }

    /**
     * Finds all claims associated with a given item.
     *
     * @param itemId The item ID.
     * @return An ArrayList of matching claims.
     */
    public ArrayList<Claim> findClaimsByItemId(int itemId) {
        ArrayList<Claim> result = new ArrayList<>();
        for (Claim claim : claims) {
            if (claim.getItemId() == itemId) {
                result.add(claim);
            }
        }
        return result;
    }

    /**
     * Alias for findClaimsByItemId.
     */
    public ArrayList<Claim> getClaimsByItemId(int itemId) {
        return findClaimsByItemId(itemId);
    }

    /**
     * Updates an existing claim in the repository.
     *
     * @param updatedClaim The claim with updated attributes.
     * @return true if found and updated, false otherwise.
     */
    public boolean updateClaim(Claim updatedClaim) {
        if (updatedClaim == null) {
            return false;
        }
        for (int i = 0; i < claims.size(); i++) {
            if (claims.get(i).getClaimId() == updatedClaim.getClaimId()) {
                claims.set(i, updatedClaim);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all claims with PENDING status.
     *
     * @return An ArrayList of pending claims.
     */
    public ArrayList<Claim> getPendingClaims() {
        ArrayList<Claim> result = new ArrayList<>();
        for (Claim claim : claims) {
            if (claim.getClaimStatus() == ClaimStatus.PENDING) {
                result.add(claim);
            }
        }
        return result;
    }

    /**
     * Returns the total count of claims stored.
     */
    public int size() {
        return claims.size();
    }
}
