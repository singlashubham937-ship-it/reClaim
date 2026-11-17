package com.reclaim.model;

import com.reclaim.enums.ItemCategory;

public class PersonalBelongingItem extends Item {

    public PersonalBelongingItem(int itemId, String name, String description, 
                                 String foundLocation, String currentLocation, int finderId) {
        super(itemId, name, description, ItemCategory.PERSONAL_BELONGING, foundLocation, currentLocation, finderId);
    }

    @Override
    public String getItemType() {
        return "Personal Belonging";
    }
}
