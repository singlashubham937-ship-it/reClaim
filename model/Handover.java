package model;

import java.time.LocalDateTime;

/**
 * Represents the physical return of a found item to its verified owner.
 *
 * Fields are kept simple and use primitive types or standard library classes.
 * The class follows the coding style used by the other team members –
 * private fields, a parameterized constructor, getters/setters and a helpful
 * {@code toString()} implementation.
 */
public class Handover {
    private String handoverId;
    private String itemId;
    private String claimId;
    private String finderId;
    private String ownerId;
    private LocalDateTime handoverDate;
    private String handoverLocation;
    private String remarks;
    private boolean confirmed;

    /**
     * Creates a handover record.
     */
    public Handover(String handoverId, String itemId, String claimId,
                    String finderId, String ownerId, LocalDateTime handoverDate,
                    String handoverLocation, String remarks, boolean confirmed) {
        this.handoverId = handoverId;
        this.itemId = itemId;
        this.claimId = claimId;
        this.finderId = finderId;
        this.ownerId = ownerId;
        this.handoverDate = handoverDate;
        this.handoverLocation = handoverLocation;
        this.remarks = remarks;
        this.confirmed = confirmed;
    }

    // Getters and setters
    public String getHandoverId() { return handoverId; }
    public void setHandoverId(String handoverId) { this.handoverId = handoverId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getFinderId() { return finderId; }
    public void setFinderId(String finderId) { this.finderId = finderId; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public LocalDateTime getHandoverDate() { return handoverDate; }
    public void setHandoverDate(LocalDateTime handoverDate) { this.handoverDate = handoverDate; }

    public String getHandoverLocation() { return handoverLocation; }
    public void setHandoverLocation(String handoverLocation) { this.handoverLocation = handoverLocation; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    /**
     * Marks the handover as confirmed.
     */
    public void confirm() {
        this.confirmed = true;
    }

    @Override
    public String toString() {
        return "Handover{" +
                "handoverId='" + handoverId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", claimId='" + claimId + '\'' +
                ", finderId='" + finderId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", handoverDate=" + handoverDate +
                ", handoverLocation='" + handoverLocation + '\'' +
                ", remarks='" + remarks + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }
}
