package com.reclaim.model;

import com.reclaim.enums.ItemCategory;

public class ElectronicsItem extends Item {

    public ElectronicsItem(int itemId, String name, String description, 
                           String foundLocation, String currentLocation, int finderId) {
        super(itemId, name, description, ItemCategory.ELECTRONICS, foundLocation, currentLocation, finderId);
    }

    @Override
    public String getItemType() {
        return "Electronics";
    }
}
