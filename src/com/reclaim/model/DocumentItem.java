package com.reclaim.model;

import com.reclaim.enums.ItemCategory;

public class DocumentItem extends Item {

    public DocumentItem(int itemId, String name, String description, 
                        String foundLocation, String currentLocation, int finderId) {
        super(itemId, name, description, ItemCategory.DOCUMENT, foundLocation, currentLocation, finderId);
    }

    @Override
    public String getItemType() {
        return "Document";
    }
}
