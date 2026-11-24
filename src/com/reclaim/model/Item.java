package com.reclaim.model;

import com.reclaim.enums.ItemCategory;
import com.reclaim.enums.ItemStatus;

public abstract class Item {
    private int itemId;
    private String name;
    private String description;
    private ItemCategory category;
    private ItemStatus status;
    private String foundLocation;
    private String currentLocation;
    private int finderId;

    public Item(int itemId, String name, String description, ItemCategory category, 
                String foundLocation, String currentLocation, int finderId) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.status = ItemStatus.AVAILABLE;
        this.foundLocation = foundLocation;
        this.currentLocation = currentLocation;
        this.finderId = finderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        if (itemId > 0) {
            this.itemId = itemId;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(String foundLocation) {
        this.foundLocation = foundLocation;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getFinderId() {
        return finderId;
    }

    public void setFinderId(int finderId) {
        if (finderId > 0) {
            this.finderId = finderId;
        }
    }

    public abstract String getItemType();

    @Override
    public String toString() {
        return "Item [itemId=" + itemId + ", name=" + name + ", description=" + description 
                + ", category=" + category + ", status=" + status 
                + ", foundLocation=" + foundLocation + ", currentLocation=" + currentLocation 
                + ", finderId=" + finderId + "]";
    }
}
