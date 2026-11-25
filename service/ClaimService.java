package service;

import model.*;
import repository.*;
import util.IdGenerator;

import java.util.ArrayList;

/**
 * ClaimService handles submitting, approving, and rejecting claims.
 *
 * OOP concept demonstrated: Encapsulation, service layer
 */
public class ClaimService {

    private ClaimRepository  claimRepository;
    private ItemRepository   itemRepository;

    public ClaimService(ClaimRepository claimRepository, ItemRepository itemRepository) {
        this.claimRepository = claimRepository;
        this.itemRepository  = itemRepository;
    }

    /**
     * A student submits a claim for a found item.
     * Returns null if the item doesn't exist or is already claimed.
     */
    public Claim submitClaim(String itemId, String claimantId, String remarks) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            System.out.println("Item not found: " + itemId);
            return null;
        }
        if (item.getStatus() == ItemStatus.CLAIMED) {
            System.out.println("This item has already been claimed.");
            return null;
        }
        // Check the student hasn't already submitted a claim for this item
        for (Claim existing : claimRepository.getAllClaims()) {
            if (existing.getItemId().equals(itemId)
                    && existing.getClaimantId().equals(claimantId)) {
                System.out.println("You have already submitted a claim for this item.");
                return null;
            }
        }
        String claimId = IdGenerator.nextClaimId();
        Claim claim = new Claim(claimId, itemId, claimantId, ClaimStatus.PENDING, remarks);
        claimRepository.addClaim(claim);
        System.out.println("Claim submitted! Claim ID: " + claimId);
        return claim;
    }

    /**
     * A finder (or admin) approves a claim.
     */
    public boolean approveClaim(String claimId) {
        Claim claim = claimRepository.getClaimById(claimId);
        if (claim == null) {
            System.out.println("Claim not found: " + claimId);
            return false;
        }
        claim.setStatus(ClaimStatus.APPROVED);
        System.out.println("Claim " + claimId + " approved.");
        return true;
    }

    /**
     * A finder (or admin) rejects a claim.
     */
    public boolean rejectClaim(String claimId) {
        Claim claim = claimRepository.getClaimById(claimId);
        if (claim == null) {
            System.out.println("Claim not found: " + claimId);
            return false;
        }
        claim.setStatus(ClaimStatus.REJECTED);
        System.out.println("Claim " + claimId + " rejected.");
        return true;
    }

    /**
     * Returns all claims for items reported by a specific finder.
     */
    public ArrayList<Claim> getClaimsForFinder(String finderId) {
        ArrayList<Claim> result = new ArrayList<>();
        for (Claim claim : claimRepository.getAllClaims()) {
            Item item = itemRepository.getItemById(claim.getItemId());
            if (item != null && finderId.equals(item.getFinderId())) {
                result.add(claim);
            }
        }
        return result;
    }

    /**
     * Returns all claims submitted by a specific student.
     */
    public ArrayList<Claim> getClaimsByStudent(String studentId) {
        ArrayList<Claim> result = new ArrayList<>();
        for (Claim claim : claimRepository.getAllClaims()) {
            if (studentId.equals(claim.getClaimantId())) {
                result.add(claim);
            }
        }
        return result;
    }

    /**
     * Looks up a single claim by ID.
     */
    public Claim getClaimById(String claimId) {
        return claimRepository.getClaimById(claimId);
    }
}
