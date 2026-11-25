package service;

import model.*;
import repository.*;
import util.DateTimeUtil;
import util.IdGenerator;

import java.util.ArrayList;

/**
 * ItemService handles found-item reporting and browsing.
 *
 * OOP concept demonstrated: Encapsulation, service layer
 */
public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Finders call this to report a new found item.
     * The initial status is FOUND (available for claiming).
     */
    public Item reportFoundItem(String title, String description,
                                String finderId, String location) {
        String itemId = IdGenerator.nextItemId();
        String today  = DateTimeUtil.todayAsString();
        Item item = new Item(itemId, title, description, finderId, location, ItemStatus.FOUND, today);
        itemRepository.addItem(item);
        System.out.println("Item reported successfully! Item ID: " + itemId);
        return item;
    }

    /**
     * Returns all items that are not yet claimed (available to browse).
     */
    public ArrayList<Item> getAvailableItems() {
        ArrayList<Item> available = new ArrayList<>();
        for (Item item : itemRepository.getAllItems()) {
            if (item.getStatus() == ItemStatus.FOUND) {
                available.add(item);
            }
        }
        return available;
    }

    /**
     * Returns all items reported by a specific finder.
     */
    public ArrayList<Item> getItemsByFinder(String finderId) {
        ArrayList<Item> result = new ArrayList<>();
        for (Item item : itemRepository.getAllItems()) {
            if (finderId.equals(item.getFinderId())) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Searches items by keyword in title or description (case-insensitive).
     */
    public ArrayList<Item> searchItems(String keyword) {
        ArrayList<Item> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Item item : itemRepository.getAllItems()) {
            if (item.getTitle().toLowerCase().contains(lowerKeyword)
                    || item.getDescription().toLowerCase().contains(lowerKeyword)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Looks up an item by its ID.
     */
    public Item getItemById(String itemId) {
        return itemRepository.getItemById(itemId);
    }
}
