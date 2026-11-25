package repository;

import model.Item;
import model.ItemStatus;
import java.util.ArrayList;

/**
 * TEMPORARY STUB for Member 1/2's ItemRepository class (for local compilation/testing).
 */
public class ItemRepository {
    private ArrayList<Item> items;

    public ItemRepository() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItemById(String itemId) {
        for (Item item : items) {
            if (itemId != null && itemId.equals(item.getItemId())) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Returns only active items (excludes CLAIMED items, while keeping them in history).
     */
    public ArrayList<Item> getActiveItems() {
        ArrayList<Item> active = new ArrayList<>();
        for (Item item : items) {
            if (item.getStatus() != ItemStatus.CLAIMED) {
                active.add(item);
            }
        }
        return active;
    }
}
