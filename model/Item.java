package model;

/**
 * TEMPORARY STUB for Member 1/2's Item class (for local compilation/testing).
 */
public class Item {
    private String itemId;
    private String title;
    private String description;
    private String finderId;
    private ItemStatus status;

    public Item(String itemId, String title, String description, String finderId, ItemStatus status) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.finderId = finderId;
        this.status = status;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFinderId() { return finderId; }
    public void setFinderId(String finderId) { this.finderId = finderId; }

    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Item{itemId='" + itemId + "', title='" + title + "', status=" + status + "}";
    }
}
