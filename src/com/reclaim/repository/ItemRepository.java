package com.reclaim.repository;

import com.reclaim.enums.ItemStatus;
import com.reclaim.model.Item;
import java.util.ArrayList;

public class ItemRepository {
    private ArrayList<Item> items;

    public ItemRepository() {
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (item == null) {
            return false;
        }
        if (getItemById(item.getItemId()) != null) {
            return false;
        }
        items.add(item);
        return true;
    }

    public Item getItemById(int itemId) {
        for (Item item : items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Item> getAllItems() {
        return items;
    }

    public ArrayList<Item> getAvailableItems() {
        ArrayList<Item> availableItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getStatus() == ItemStatus.AVAILABLE) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    public boolean removeItem(int itemId) {
        Item item = getItemById(itemId);
        if (item != null) {
            items.remove(item);
            return true;
        }
        return false;
    }
}
