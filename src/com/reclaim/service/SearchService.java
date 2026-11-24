package com.reclaim.service;

import com.reclaim.enums.ItemCategory;
import com.reclaim.enums.ItemStatus;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Item;
import com.reclaim.repository.ItemRepository;

import java.util.ArrayList;

/**
 * Service providing search and filtering capabilities over lost and found items.
 * Uses Member 1's ItemRepository and enforces status, location, and category filtering.
 * Adheres to object-oriented principles by separating search predicates and display logic.
 */
public class SearchService implements Searchable<Item> {

    private final ItemRepository itemRepository;

    /**
     * Constructs a SearchService backed by the specified ItemRepository.
     *
     * @param itemRepository The repository holding items.
     */
    public SearchService(ItemRepository itemRepository) {
        if (itemRepository == null) {
            throw new IllegalArgumentException("ItemRepository cannot be null.");
        }
        this.itemRepository = itemRepository;
    }

    /**
     * Default constructor initializing with an empty ItemRepository.
     */
    public SearchService() {
        this(new ItemRepository());
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    // -------------------------------------------------------------
    // Searchable<Item> Implementation
    // -------------------------------------------------------------

    /**
     * Searches for an item by its unique ID.
     * Only returns the item if its status is AVAILABLE.
     */
    @Override
    public Item searchById(int id) {
        Item item = itemRepository.getItemById(id);
        if (item != null && isAvailable(item)) {
            return item;
        }
        return null;
    }

    /**
     * Searches for items matching the given name (case-insensitive, partial matching).
     * Returns only items with AVAILABLE status.
     */
    @Override
    public ArrayList<Item> searchByName(String name) {
        ArrayList<Item> results = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            return results;
        }

        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item) && matchesName(item, name)) {
                results.add(item);
            }
        }
        return results;
    }

    // -------------------------------------------------------------
    // Location, Category, and Keyword Search Methods
    // -------------------------------------------------------------

    /**
     * Searches for available items by location name (foundLocation or currentLocation).
     * Case-insensitive, partial matching.
     */
    public ArrayList<Item> searchByLocation(String location) {
        ArrayList<Item> results = new ArrayList<>();
        if (location == null || location.trim().isEmpty()) {
            return results;
        }

        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item) && matchesLocation(item, location)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Searches for available items matching the name of a CampusLocation.
     */
    public ArrayList<Item> searchByLocation(CampusLocation location) {
        if (location == null || location.getLocationName() == null) {
            return new ArrayList<>();
        }
        return searchByLocation(location.getLocationName());
    }

    /**
     * Searches for available items matching a specific ItemCategory.
     */
    public ArrayList<Item> searchByCategory(ItemCategory category) {
        ArrayList<Item> results = new ArrayList<>();
        if (category == null) {
            return results;
        }

        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item) && matchesCategory(item, category)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Searches for available items by category name string (case-insensitive).
     */
    public ArrayList<Item> searchByCategory(String categoryName) {
        ArrayList<Item> results = new ArrayList<>();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return results;
        }

        ItemCategory matchedCategory = null;
        for (ItemCategory cat : ItemCategory.values()) {
            if (cat.name().equalsIgnoreCase(categoryName.trim()) 
                    || cat.name().replace('_', ' ').equalsIgnoreCase(categoryName.trim())) {
                matchedCategory = cat;
                break;
            }
        }

        if (matchedCategory != null) {
            return searchByCategory(matchedCategory);
        }

        // Fallback: substring matching on category name
        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item) && item.getCategory() != null
                    && item.getCategory().name().toLowerCase().contains(categoryName.toLowerCase().trim())) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Searches for available items using a free-form keyword.
     * Matches name, description, category, and location fields.
     */
    public ArrayList<Item> searchByKeyword(String keyword) {
        ArrayList<Item> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item) && matchesKeyword(item, keyword)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Combined search filtering items by both campus location and category.
     */
    public ArrayList<Item> searchByLocationAndCategory(String location, ItemCategory category) {
        ArrayList<Item> results = new ArrayList<>();
        for (Item item : itemRepository.getAllItems()) {
            if (isAvailable(item)
                    && (location == null || matchesLocation(item, location))
                    && (category == null || matchesCategory(item, category))) {
                results.add(item);
            }
        }
        return results;
    }

    // -------------------------------------------------------------
    // Display Methods
    // -------------------------------------------------------------

    /**
     * Displays search results with item ID, name, category, found location,
     * current location, and description.
     * Handles empty results gracefully.
     */
    public void displaySearchResults(ArrayList<Item> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("No available items found matching the search criteria.");
            System.out.println("--------------------------------------------------------------------------------");
            return;
        }

        System.out.println("================================================================================");
        System.out.println("                        SEARCH RESULTS (" + results.size() + " Item(s) Found)                   ");
        System.out.println("================================================================================");
        System.out.printf("%-8s | %-22s | %-16s | %-14s | %-14s%n",
                "Item ID", "Name", "Category", "Found Location", "Current Location");
        System.out.println("--------------------------------------------------------------------------------");

        for (Item item : results) {
            System.out.printf("%-8d | %-22s | %-16s | %-14s | %-14s%n",
                    item.getItemId(),
                    truncate(item.getName(), 22),
                    item.getCategory() != null ? item.getCategory().name() : "N/A",
                    truncate(item.getFoundLocation(), 14),
                    truncate(item.getCurrentLocation(), 14));
            System.out.println("  Description      : " + (item.getDescription() != null ? item.getDescription() : "None"));
            System.out.println("  Item Type / Class: " + item.getItemType());
            System.out.println("  Status           : " + item.getStatus());
            System.out.println("--------------------------------------------------------------------------------");
        }
    }

    /**
     * Displays a single item search result.
     */
    public void displaySearchResults(Item item) {
        if (item == null) {
            displaySearchResults(new ArrayList<>());
            return;
        }
        ArrayList<Item> singleList = new ArrayList<>();
        singleList.add(item);
        displaySearchResults(singleList);
    }

    // -------------------------------------------------------------
    // Private Helper Predicates (Object-Oriented Design)
    // -------------------------------------------------------------

    /**
     * Validates that an item is AVAILABLE.
     * CLAIMED and CLAIM_PENDING items are excluded from active search results.
     */
    private boolean isAvailable(Item item) {
        return item != null && item.getStatus() == ItemStatus.AVAILABLE;
    }

    private boolean matchesName(Item item, String nameQuery) {
        if (item.getName() == null) {
            return false;
        }
        return item.getName().toLowerCase().contains(nameQuery.toLowerCase().trim());
    }

    private boolean matchesLocation(Item item, String locationQuery) {
        String query = locationQuery.toLowerCase().trim();
        boolean matchesFound = item.getFoundLocation() != null 
                && item.getFoundLocation().toLowerCase().contains(query);
        boolean matchesCurrent = item.getCurrentLocation() != null 
                && item.getCurrentLocation().toLowerCase().contains(query);
        return matchesFound || matchesCurrent;
    }

    private boolean matchesCategory(Item item, ItemCategory category) {
        return item.getCategory() == category;
    }

    private boolean matchesKeyword(Item item, String keyword) {
        String query = keyword.toLowerCase().trim();
        return matchesName(item, query)
                || (item.getDescription() != null && item.getDescription().toLowerCase().contains(query))
                || matchesLocation(item, query)
                || (item.getCategory() != null && item.getCategory().name().toLowerCase().contains(query));
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "N/A";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
