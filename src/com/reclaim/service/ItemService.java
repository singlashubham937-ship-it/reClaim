package com.reclaim.service;

import com.reclaim.enums.ItemStatus;
import com.reclaim.model.Item;
import com.reclaim.repository.ItemRepository;
import java.util.ArrayList;

public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Report a found item (validate before saving)
    public boolean reportFoundItem(Item item) {
        if (item == null) {
            System.out.println("Item cannot be null.");
            return false;
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return false;
        }
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            System.out.println("Item description cannot be empty.");
            return false;
        }

        // Status is set to AVAILABLE by default in the Item constructor
        // but we set it explicitly here to be safe
        item.setStatus(ItemStatus.AVAILABLE);

        boolean added = itemRepository.addItem(item);
        if (added) {
            System.out.println("Item reported successfully: " + item.getName() + " (ID: " + item.getItemId() + ")");
        } else {
            System.out.println("Failed to add item. An item with ID " + item.getItemId() + " already exists.");
        }
        return added;
    }

    // Get an item by its ID
    public Item getItemById(int itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            System.out.println("No item found with ID: " + itemId);
        }
        return item;
    }

    // Get all items
    public ArrayList<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    // Get only items with AVAILABLE status
    public ArrayList<Item> getAvailableItems() {
        return itemRepository.getAvailableItems();
    }

    // Update item name and description
    public boolean updateItemDetails(int itemId, String newName, String newDescription) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            System.out.println("Item not found with ID: " + itemId);
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            item.setName(newName);
        }
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            item.setDescription(newDescription);
        }
        System.out.println("Item details updated for ID: " + itemId);
        return true;
    }

    // Update the status of an item
    public boolean updateItemStatus(int itemId, ItemStatus newStatus) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            System.out.println("Item not found with ID: " + itemId);
            return false;
        }
        if (newStatus == null) {
            System.out.println("Status cannot be null.");
            return false;
        }
        item.setStatus(newStatus);
        System.out.println("Item status updated to " + newStatus + " for item ID: " + itemId);
        return true;
    }
}
