package service;

import model.AuditLog;
import model.Claim;
import model.ClaimStatus;
import model.Handover;
import model.Item;
import model.ItemStatus;
import repository.ClaimRepository;
import repository.HandoverRepository;
import repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Handles the business logic for handing over an approved claimed item
 * to its verified owner and recording audit logs.
 */
public class HandoverService {
    private HandoverRepository handoverRepository;
    private ItemRepository itemRepository;
    private ClaimRepository claimRepository;
    private ArrayList<AuditLog> auditLogs;
    private int handoverCounter;
    private int logCounter;

    public HandoverService(HandoverRepository handoverRepository,
                           ItemRepository itemRepository,
                           ClaimRepository claimRepository) {
        this.handoverRepository = handoverRepository;
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
        this.auditLogs = new ArrayList<>();
        this.handoverCounter = 1;
        this.logCounter = 1;
    }

    /**
     * Starts a handover for an approved claim after validating all business rules.
     */
    public Handover startHandover(String claimId, String handoverLocation, String remarks) {
        Claim claim = claimRepository.getClaimById(claimId);
        if (claim == null) {
            throw new IllegalArgumentException("Claim not found for ID: " + claimId);
        }

        if (claim.getStatus() != ClaimStatus.APPROVED) {
            throw new IllegalStateException("Only an APPROVED claim can proceed to handover. Current status: " + claim.getStatus());
        }

        Item item = itemRepository.getItemById(claim.getItemId());
        if (item == null) {
            throw new IllegalArgumentException("Item not found for ID: " + claim.getItemId());
        }

        if (item.getStatus() == ItemStatus.CLAIMED) {
            throw new IllegalStateException("Item is already CLAIMED: " + item.getItemId());
        }

        ArrayList<Handover> existingHandovers = handoverRepository.getHandoversByItemId(item.getItemId());
        if (!existingHandovers.isEmpty()) {
            throw new IllegalStateException("Duplicate handover not allowed for item: " + item.getItemId());
        }

        String handoverId = "HO-" + handoverCounter;
        handoverCounter++;

        Handover handover = new Handover(
                handoverId,
                item.getItemId(),
                claim.getClaimId(),
                item.getFinderId(),
                claim.getClaimantId(),
                LocalDateTime.now(),
                handoverLocation,
                remarks,
                false
        );

        boolean saved = handoverRepository.addHandover(handover);
        if (!saved) {
            throw new IllegalStateException("Could not save handover with ID: " + handoverId);
        }

        recordAuditLog(
                claim.getClaimantId(),
                "HANDOVER_STARTED",
                "Started handover " + handoverId + " for item " + item.getItemId() + " and claim " + claim.getClaimId()
        );

        return handover;
    }

    /**
     * Confirms the physical handover and marks the item as CLAIMED.
     */
    public boolean confirmHandover(String handoverId, String confirmedByUserId) {
        Handover handover = handoverRepository.getHandoverById(handoverId);
        if (handover == null) {
            throw new IllegalArgumentException("Handover not found for ID: " + handoverId);
        }

        if (handover.isConfirmed()) {
            throw new IllegalStateException("Handover is already confirmed: " + handoverId);
        }

        Item item = itemRepository.getItemById(handover.getItemId());
        if (item == null) {
            throw new IllegalArgumentException("Item not found for ID: " + handover.getItemId());
        }

        if (item.getStatus() == ItemStatus.CLAIMED) {
            throw new IllegalStateException("Item is already CLAIMED: " + item.getItemId());
        }

        handover.confirm();
        handover.setHandoverDate(LocalDateTime.now());

        recordAuditLog(
                confirmedByUserId,
                "HANDOVER_CONFIRMED",
                "Confirmed physical handover " + handoverId + " for item " + item.getItemId()
        );

        item.setStatus(ItemStatus.CLAIMED);

        recordAuditLog(
                confirmedByUserId,
                "ITEM_CLAIMED",
                "Marked item " + item.getItemId() + " as CLAIMED"
        );

        return true;
    }

    /**
     * Returns all handovers associated with a user (as finder or owner).
     * This helper is used by FinderMenu and StudentMenu.
     */
    public ArrayList<Handover> getHandoversByUser(String userId) {
        return handoverRepository.getHandoversByUserId(userId);
    }

    private void recordAuditLog(String userId, String action, String description) {
        String logId = "LOG-" + logCounter;
        logCounter++;
        AuditLog log = new AuditLog(logId, userId, action, description, LocalDateTime.now());
        auditLogs.add(log);
    }

    public ArrayList<AuditLog> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }
}
