package com.reclaim.service;

import com.reclaim.exception.ItemNotFoundException;
import com.reclaim.exception.UnauthorizedActionException;
import com.reclaim.model.Claim;
import com.reclaim.model.Item;
import com.reclaim.model.OwnershipProof;
import com.reclaim.model.VerificationResult;
import com.reclaim.repository.ClaimRepository;
import com.reclaim.repository.ItemRepository;

/**
 * Service managing ownership verification by authorized finders.
 * Compares student-submitted OwnershipProof against item details,
 * computes confidence scores, and produces clear verification remarks.
 */
public class VerificationService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;
    private final ClaimService claimService;

    public VerificationService(ClaimRepository claimRepository, ItemRepository itemRepository, 
                               NotificationService notificationService, ClaimService claimService) {
        if (claimRepository == null || itemRepository == null) {
            throw new IllegalArgumentException("Repositories cannot be null.");
        }
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
        this.notificationService = notificationService;
        this.claimService = claimService;
    }

    /**
     * Verifies the ownership proof of a claim.
     * Enforces that only the designated finder of the item can perform verification.
     *
     * @param claimId    The claim ID.
     * @param verifierId User ID of the finder attempting verification.
     * @return VerificationResult containing score, outcome, and detailed remarks.
     */
    public VerificationResult verifyClaim(int claimId, int verifierId) {
        Claim claim = claimRepository.findClaimById(claimId);
        if (claim == null) {
            throw new IllegalArgumentException("Claim with ID " + claimId + " not found.");
        }

        Item item = itemRepository.getItemById(claim.getItemId());
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + claim.getItemId() + " not found.");
        }

        // Only the authorized finder associated with the item can verify
        if (item.getFinderId() != verifierId) {
            throw new UnauthorizedActionException("User " + verifierId + " is not authorized to verify claim " 
                    + claimId + ". Only designated finder (User ID: " + item.getFinderId() + ") can verify.");
        }

        OwnershipProof proof = claim.getOwnershipProof();
        if (proof == null || !proof.validateProof()) {
            return new VerificationResult(claimId, item.getItemId(), verifierId, false, 0,
                    "Incomplete ownership proof provided. Colour and description are mandatory.");
        }

        // Evaluate proof attributes against item data
        int score = 0;
        StringBuilder remarksBuilder = new StringBuilder();

        String itemFullText = ((item.getName() != null ? item.getName() : "") + " " 
                + (item.getDescription() != null ? item.getDescription() : "")).toLowerCase();

        // 1. Colour verification
        if (proof.getItemColour() != null && !proof.getItemColour().trim().isEmpty()) {
            if (itemFullText.contains(proof.getItemColour().toLowerCase().trim())) {
                score += 35;
                remarksBuilder.append("[Colour matched: ").append(proof.getItemColour()).append("] ");
            } else {
                remarksBuilder.append("[Colour did not match item record] ");
            }
        }

        // 2. Brand verification
        if (proof.getBrand() != null && !proof.getBrand().trim().isEmpty()) {
            if (itemFullText.contains(proof.getBrand().toLowerCase().trim())) {
                score += 35;
                remarksBuilder.append("[Brand matched: ").append(proof.getBrand()).append("] ");
            } else {
                remarksBuilder.append("[Brand not mentioned in item record] ");
            }
        }

        // 3. Identifying description / unique markers
        if (proof.getIdentifyingDescription() != null && !proof.getIdentifyingDescription().trim().isEmpty()) {
            String[] tokens = proof.getIdentifyingDescription().toLowerCase().split("\\s+");
            int tokenMatches = 0;
            for (String token : tokens) {
                if (token.length() > 3 && itemFullText.contains(token)) {
                    tokenMatches++;
                }
            }
            if (tokenMatches > 0) {
                score += 20;
                remarksBuilder.append("[Identifying description has ").append(tokenMatches).append(" keyword match(es)] ");
            }
        }

        // 4. Additional details bonus
        if (proof.getAdditionalOwnershipDetails() != null && !proof.getAdditionalOwnershipDetails().trim().isEmpty()) {
            score += 10;
            remarksBuilder.append("[Additional proof provided: ").append(proof.getAdditionalOwnershipDetails()).append("] ");
        }

        boolean verified = score >= 50;
        String finalRemarks = (verified ? "Verification SUCCESSFUL. " : "Verification INCONCLUSIVE. ") 
                + remarksBuilder.toString().trim();

        VerificationResult result = new VerificationResult(claimId, item.getItemId(), verifierId, verified, score, finalRemarks);

        // Notify claimant of verification review
        if (notificationService != null) {
            notificationService.sendNotification(claim.getStudentId(),
                    "Your claim (ID " + claimId + ") for item '" + item.getName() 
                    + "' was reviewed by the finder. Outcome: " + (verified ? "PASSED" : "NEEDS CLARIFICATION"));
        }

        return result;
    }

    /**
     * Verifies and approves a claim in a single step if verification passes.
     */
    public boolean verifyAndApprove(int claimId, int verifierId, String remarks) {
        VerificationResult result = verifyClaim(claimId, verifierId);
        String combinedRemarks = result.getRemarks() + (remarks != null && !remarks.isEmpty() ? " | " + remarks : "");
        if (claimService != null) {
            return claimService.approveClaim(claimId, verifierId, combinedRemarks);
        }
        return false;
    }

    /**
     * Verifies and rejects a claim with verification remarks.
     */
    public boolean verifyAndReject(int claimId, int verifierId, String remarks) {
        VerificationResult result = verifyClaim(claimId, verifierId);
        String combinedRemarks = result.getRemarks() + (remarks != null && !remarks.isEmpty() ? " | " + remarks : "");
        if (claimService != null) {
            return claimService.rejectClaim(claimId, verifierId, combinedRemarks);
        }
        return false;
    }
}
