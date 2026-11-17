package com.reclaim.model;

import com.reclaim.enums.ItemCategory;

public class AccessoryItem extends Item {

    public AccessoryItem(int itemId, String name, String description, 
                         String foundLocation, String currentLocation, int finderId) {
        super(itemId, name, description, ItemCategory.ACCESSORY, foundLocation, currentLocation, finderId);
    }

    @Override
    public String getItemType() {
        return "Accessory";
    }
}
