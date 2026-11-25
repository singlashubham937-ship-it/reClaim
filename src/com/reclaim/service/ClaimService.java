package com.reclaim.service;

import com.reclaim.enums.ClaimStatus;
import com.reclaim.enums.ItemStatus;
import com.reclaim.exception.DuplicateClaimException;
import com.reclaim.exception.ItemNotFoundException;
import com.reclaim.exception.UnauthorizedActionException;
import com.reclaim.model.Claim;
import com.reclaim.model.Item;
import com.reclaim.model.OwnershipProof;
import com.reclaim.repository.ClaimRepository;
import com.reclaim.repository.ItemRepository;

import java.util.ArrayList;

/**
 * Service managing item claims, duplicate checking, and verification workflows in reClaim.
 * Integrates with Member 1's ItemRepository and generates user notifications.
 */
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;
    private NotificationService notificationService;
    private int nextClaimId = 1;

    public ClaimService(ClaimRepository claimRepository, ItemRepository itemRepository) {
        if (claimRepository == null) {
            throw new IllegalArgumentException("ClaimRepository cannot be null.");
        }
        if (itemRepository == null) {
            throw new IllegalArgumentException("ItemRepository cannot be null.");
        }
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
    }

    public ClaimService(ClaimRepository claimRepository, ItemRepository itemRepository, NotificationService notificationService) {
        this(claimRepository, itemRepository);
        this.notificationService = notificationService;
    }

    public ClaimService() {
        this(new ClaimRepository(), new ItemRepository());
    }

    public ClaimRepository getClaimRepository() {
        return claimRepository;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Submits a new claim on an item.
     *
     * Business Rules enforced:
     * - Item must exist (throws ItemNotFoundException).
     * - Prevent duplicate active claims by same student for same item (throws DuplicateClaimException).
     * - Item must be AVAILABLE (throws IllegalStateException).
     * - Ownership proof cannot be null or incomplete (throws IllegalArgumentException).
     * - New claim always starts with PENDING status.
     * - Updates item status to CLAIM_PENDING.
     * - Generates notification for student and finder.
     *
     * @param itemId         ID of the item being claimed.
     * @param studentId      ID of the claiming student.
     * @param ownershipProof Proof submitted by claimant.
     * @return The created Claim object.
     */
    public Claim submitClaim(int itemId, int studentId, OwnershipProof ownershipProof) {
        // 1. A student can only claim an item that exists
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + itemId + " does not exist.");
        }

        // 2. Prevent duplicate active claims by the same student for the same item
        if (checkDuplicateClaim(itemId, studentId)) {
            throw new DuplicateClaimException("Student with ID " + studentId + " already has an active claim on item ID " + itemId + ".");
        }

        // 3. An item must have AVAILABLE status before a claim is submitted
        if (item.getStatus() != ItemStatus.AVAILABLE) {
            throw new IllegalStateException("Item with ID " + itemId + " is not available for claiming. Current status: " + item.getStatus());
        }

        // 4. Validate ownership proof
        if (ownershipProof == null) {
            throw new IllegalArgumentException("Ownership proof cannot be null for a valid claim.");
        }
        if (!ownershipProof.validateProof()) {
            throw new IllegalArgumentException("Ownership proof is incomplete. Colour and identifying description are required.");
        }

        // 5. Create claim with PENDING status
        int claimId = getNextClaimId();
        Claim claim = new Claim(claimId, itemId, studentId, item.getFinderId(), ownershipProof);

        // 6. Set item status to CLAIM_PENDING
        item.setStatus(ItemStatus.CLAIM_PENDING);

        // 7. Save to repository
        claimRepository.addClaim(claim);

        // 8. Generate notifications
        if (notificationService != null) {
            notificationService.sendNotification(studentId,
                    "Claim #" + claimId + " submitted successfully for item '" + item.getName() + "' (ID: " + itemId + "). Status: PENDING.");
            if (item.getFinderId() > 0) {
                notificationService.sendNotification(item.getFinderId(),
                        "New claim #" + claimId + " submitted by Student ID " + studentId + " for item '" + item.getName() + "'.");
            }
        }

        return claim;
    }

    /**
     * Returns all claims submitted by a given student.
     */
    public ArrayList<Claim> viewMyClaims(int studentId) {
        return claimRepository.findClaimsByStudentId(studentId);
    }

    /**
     * Alias for viewMyClaims matching naming requirements.
     */
    public ArrayList<Claim> viewStudentClaims(int studentId) {
        return viewMyClaims(studentId);
    }

    /**
     * Returns all claims currently in PENDING status awaiting review.
     */
    public ArrayList<Claim> viewPendingClaims() {
        return claimRepository.getPendingClaims();
    }

    /**
     * Retrieves full details of a specific claim.
     */
    public Claim getClaimDetails(int claimId) {
        return claimRepository.getClaimById(claimId);
    }

    /**
     * Approves a pending claim.
     *
     * Business Rules:
     * - Only the finder associated with the reported item can approve its claim.
     * - Claim must be in PENDING status.
     * - Item is NOT marked CLAIMED yet (handover is Member 3's responsibility).
     *
     * @param claimId    The claim ID.
     * @param approverId The ID of the user attempting approval (must match finderId).
     * @param remarks    Approval remarks or notes.
     * @return true if approved successfully.
     */
    public boolean approveClaim(int claimId, int approverId, String remarks) {
        Claim claim = claimRepository.findClaimById(claimId);
        if (claim == null) {
            throw new IllegalArgumentException("Claim with ID " + claimId + " not found.");
        }

        Item item = itemRepository.getItemById(claim.getItemId());
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + claim.getItemId() + " associated with claim does not exist.");
        }

        // Only the finder associated with the reported item can approve its claim
        if (item.getFinderId() != approverId) {
            throw new UnauthorizedActionException("User " + approverId + " is not authorized to approve claim " 
                    + claimId + ". Only the designated finder (User ID: " + item.getFinderId() + ") can approve.");
        }

        // Approve the claim (enforces PENDING status rule in Claim model)
        claim.approveClaim(remarks);
        claimRepository.updateClaim(claim);

        // Item remains CLAIM_PENDING - Handover / CLAIMED status is Member 3's responsibility

        // Notify student of approval
        if (notificationService != null) {
            notificationService.sendNotification(claim.getStudentId(),
                    "Good news! Your claim #" + claimId + " for item '" + item.getName() 
                    + "' has been APPROVED by the finder. Remarks: " + remarks);
        }

        return true;
    }

    /**
     * Rejects a pending claim.
     *
     * Business Rules:
     * - Only the finder associated with the reported item can reject its claim.
     * - Claim must be in PENDING status.
     * - If no other active claims exist, item reverts to AVAILABLE.
     *
     * @param claimId    The claim ID.
     * @param rejecterId The ID of the user attempting rejection (must match finderId).
     * @param remarks    Rejection reason.
     * @return true if rejected successfully.
     */
    public boolean rejectClaim(int claimId, int rejecterId, String remarks) {
        Claim claim = claimRepository.findClaimById(claimId);
        if (claim == null) {
            throw new IllegalArgumentException("Claim with ID " + claimId + " not found.");
        }

        Item item = itemRepository.getItemById(claim.getItemId());
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + claim.getItemId() + " associated with claim does not exist.");
        }

        // Only the finder associated with the reported item can reject its claim
        if (item.getFinderId() != rejecterId) {
            throw new UnauthorizedActionException("User " + rejecterId + " is not authorized to reject claim " 
                    + claimId + ". Only the designated finder (User ID: " + item.getFinderId() + ") can reject.");
        }

        // Reject the claim (enforces PENDING status rule in Claim model)
        claim.rejectClaim(remarks);
        claimRepository.updateClaim(claim);

        // If no other active claims exist for this item, restore status to AVAILABLE
        boolean hasOtherActiveClaims = false;
        for (Claim c : claimRepository.findClaimsByItemId(item.getItemId())) {
            if (c.getClaimStatus() == ClaimStatus.PENDING || c.getClaimStatus() == ClaimStatus.APPROVED) {
                hasOtherActiveClaims = true;
                break;
            }
        }
        if (!hasOtherActiveClaims) {
            item.setStatus(ItemStatus.AVAILABLE);
        }

        // Notify student of rejection
        if (notificationService != null) {
            notificationService.sendNotification(claim.getStudentId(),
                    "Update: Your claim #" + claimId + " for item '" + item.getName() 
                    + "' was REJECTED by the finder. Reason: " + remarks);
        }

        return true;
    }

    /**
     * Checks if an active claim already exists by the same student for the same item.
     * Active claims are those with status PENDING or APPROVED.
     */
    public boolean checkDuplicateClaim(int itemId, int studentId) {
        ArrayList<Claim> studentClaims = claimRepository.findClaimsByStudentId(studentId);
        for (Claim claim : studentClaims) {
            if (claim.getItemId() == itemId 
                    && (claim.getClaimStatus() == ClaimStatus.PENDING || claim.getClaimStatus() == ClaimStatus.APPROVED)) {
                return true;
            }
        }
        return false;
    }

    private synchronized int getNextClaimId() {
        return nextClaimId++;
    }
}
