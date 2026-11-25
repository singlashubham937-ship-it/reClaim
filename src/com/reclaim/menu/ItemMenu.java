package com.reclaim.menu;

import com.reclaim.model.Item;
import com.reclaim.repository.ItemRepository;
import com.reclaim.service.SearchService;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console menu dedicated to searching and inspecting lost and found items.
 */
public class ItemMenu {

    private final SearchService searchService;
    private final ItemRepository itemRepository;

    public ItemMenu(SearchService searchService, ItemRepository itemRepository) {
        this.searchService = searchService;
        this.itemRepository = itemRepository;
    }

    public void displayMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n================================================================================");
            System.out.println("                         ITEM SEARCH & BROWSE MENU                              ");
            System.out.println("================================================================================");
            System.out.println(" 1. Search item by ID");
            System.out.println(" 2. Search item by name");
            System.out.println(" 3. Search by location");
            System.out.println(" 4. Search by category");
            System.out.println(" 5. View item details");
            System.out.println(" 6. Return to previous menu");
            System.out.println("================================================================================");
            System.out.print("Enter choice (1-6): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleSearchById(scanner);
                    break;
                case "2":
                    handleSearchByName(scanner);
                    break;
                case "3":
                    handleSearchByLocation(scanner);
                    break;
                case "4":
                    handleSearchByCategory(scanner);
                    break;
                case "5":
                    handleViewItemDetails(scanner);
                    break;
                case "6":
                    System.out.println("Returning to previous menu...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    break;
            }
        }
    }

    private void handleSearchById(Scanner scanner) {
        System.out.print("Enter Item ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Item item = searchService.searchById(id);
            if (item != null) {
                searchService.displaySearchResults(item);
            } else {
                System.out.println("No available item found with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid number.");
        }
    }

    private void handleSearchByName(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByName(name);
        searchService.displaySearchResults(results);
    }

    private void handleSearchByLocation(Scanner scanner) {
        System.out.print("Enter campus location: ");
        String location = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByLocation(location);
        searchService.displaySearchResults(results);
    }

    private void handleSearchByCategory(Scanner scanner) {
        System.out.println("Categories: ELECTRONICS, DOCUMENT, PERSONAL_BELONGING, ACCESSORY, OTHER");
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        ArrayList<Item> results = searchService.searchByCategory(category);
        searchService.displaySearchResults(results);
    }

    private void handleViewItemDetails(Scanner scanner) {
        System.out.print("Enter Item ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Item item = itemRepository.getItemById(id);
            if (item != null) {
                searchService.displaySearchResults(item);
            } else {
                System.out.println("Item with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid number.");
        }
    }
}
