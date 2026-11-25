package model;

/**
 * Represents a found item reported by a finder in the reClaim system.
 *
 * OOP concept demonstrated: Encapsulation
 */
public class Item {

    private String itemId;
    private String title;
    private String description;
    private String finderId;        // The user who found and reported this item
    private String location;        // Where the item was found
    private ItemStatus status;      // FOUND, CLAIMED, etc.
    private String foundDate;       // Date as a formatted string

    /**
     * Full constructor.
     */
    public Item(String itemId, String title, String description, String finderId,
                String location, ItemStatus status, String foundDate) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.finderId = finderId;
        this.location = location;
        this.status = status;
        this.foundDate = foundDate;
    }

    /**
     * Short constructor (for backward-compatibility with existing test code).
     */
    public Item(String itemId, String title, String description, String finderId, ItemStatus status) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.finderId = finderId;
        this.location = "Unknown";
        this.status = status;
        this.foundDate = "";
    }

    // ---- Getters & Setters ----

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFinderId() { return finderId; }
    public void setFinderId(String finderId) { this.finderId = finderId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }

    public String getFoundDate() { return foundDate; }
    public void setFoundDate(String foundDate) { this.foundDate = foundDate; }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", foundDate='" + foundDate + '\'' +
                '}';
    }
}
