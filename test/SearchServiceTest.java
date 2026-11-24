package test;

import com.reclaim.enums.ItemCategory;
import com.reclaim.enums.ItemStatus;
import com.reclaim.model.AccessoryItem;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.DocumentItem;
import com.reclaim.model.ElectronicsItem;
import com.reclaim.model.Item;
import com.reclaim.model.PersonalBelongingItem;
import com.reclaim.repository.ItemRepository;
import com.reclaim.service.SearchService;
import com.reclaim.service.Searchable;

import java.util.ArrayList;

/**
 * Test suite verifying the SearchService and Searchable interface implementation.
 */
public class SearchServiceTest {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("          reClaim - University Lost and Found Management System                 ");
        System.out.println("          MEMBER 2: SEARCH SERVICE & SEARCHABLE INTERFACE TESTS                 ");
        System.out.println("================================================================================\n");

        int testsPassed = 0;
        int totalTests = 0;

        // 1. Setup sample items in ItemRepository
        ItemRepository repository = new ItemRepository();

        // Sample 1: Leather Wallet (Personal Belonging, Library, AVAILABLE)
        Item wallet = new PersonalBelongingItem(
                101,
                "Brown Leather Wallet",
                "Tommy Hilfiger wallet containing student ID card and Metro pass",
                "Central Library 2nd Floor",
                "Library Security Desk",
                1
        );

        // Sample 2: Scientific Calculator (Electronics, Library, AVAILABLE)
        Item calculator = new ElectronicsItem(
                102,
                "Casio Scientific Calculator",
                "Casio fx-991EX model with name sticker 'Alex' on back",
                "Central Library Reading Room",
                "Library Security Desk",
                2
        );

        // Sample 3: Dell Laptop (Electronics, Tech Park, AVAILABLE)
        Item laptop = new ElectronicsItem(
                103,
                "Dell Inspiron 15 Laptop",
                "Silver color laptop with university IT sticker",
                "Tech Park Block A - Room 204",
                "Campus Lost & Found Office",
                3
        );

        // Sample 4: Student ID Card (Document, Cafeteria, AVAILABLE)
        Item idCard = new DocumentItem(
                104,
                "Student ID Card",
                "Computer Science sophomore ID card under name 'Bhoomika'",
                "Main Cafeteria",
                "Student Welfare Center",
                4
        );

        // Sample 5: Wrist Watch (Accessory, Sports Complex, CLAIMED - Should be EXCLUDED)
        Item watch = new AccessoryItem(
                105,
                "Fossil Chronograph Watch",
                "Black strap chronograph watch",
                "Sports Complex Badminton Court",
                "Claimed by Owner",
                5
        );
        watch.setStatus(ItemStatus.CLAIMED); // Status is CLAIMED!

        // Sample 6: Wireless Earbuds (Electronics, Library, CLAIM_PENDING - Should be EXCLUDED)
        Item earbuds = new ElectronicsItem(
                106,
                "Apple AirPods Pro",
                "White case with blue silicone cover",
                "Central Library 1st Floor",
                "Security Office",
                6
        );
        earbuds.setStatus(ItemStatus.CLAIM_PENDING); // Status is CLAIM_PENDING!

        // Populate repository
        repository.addItem(wallet);
        repository.addItem(calculator);
        repository.addItem(laptop);
        repository.addItem(idCard);
        repository.addItem(watch);
        repository.addItem(earbuds);

        SearchService searchService = new SearchService(repository);

        // -------------------------------------------------------------
        // TEST 1: Searching for a wallet (partial & case-insensitive)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 1] Searching for a wallet by name (case-insensitive & partial match)");
        ArrayList<Item> walletResults = searchService.searchByName("wallet");
        System.out.println("  Search query: 'wallet'");
        searchService.displaySearchResults(walletResults);

        if (walletResults.size() == 1 && walletResults.get(0).getItemId() == 101) {
            System.out.println("  PASS: Exactly 1 available wallet found.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Wallet search did not return expected item.");
        }

        // -------------------------------------------------------------
        // TEST 2: Searching in the library (location-based search)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 2] Searching by location ('Library')");
        ArrayList<Item> libraryResults = searchService.searchByLocation("Library");
        System.out.println("  Search query: 'Library'");
        searchService.displaySearchResults(libraryResults);

        // Should return wallet (101) and calculator (102), but NOT earbuds (106 - CLAIM_PENDING)
        boolean hasWallet = libraryResults.stream().anyMatch(i -> i.getItemId() == 101);
        boolean hasCalc = libraryResults.stream().anyMatch(i -> i.getItemId() == 102);
        boolean hasEarbuds = libraryResults.stream().anyMatch(i -> i.getItemId() == 106);

        if (libraryResults.size() == 2 && hasWallet && hasCalc && !hasEarbuds) {
            System.out.println("  PASS: Found available items in Library (CLAIM_PENDING items excluded).");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Location search results incorrect.");
        }

        // Test with CampusLocation object overload
        CampusLocation libraryLocation = new CampusLocation(1, "Central Library", "Main Academic Block", "University Central Library");
        ArrayList<Item> campusLocResults = searchService.searchByLocation(libraryLocation);
        if (campusLocResults.size() == 2) {
            System.out.println("  PASS: CampusLocation object search returned identical results.");
        } else {
            System.err.println("  FAIL: CampusLocation object search failed.");
        }

        // -------------------------------------------------------------
        // TEST 3: Searching by electronics category
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 3] Searching by Category (ELECTRONICS)");
        ArrayList<Item> electronicsResults = searchService.searchByCategory(ItemCategory.ELECTRONICS);
        System.out.println("  Search query: Category = ELECTRONICS");
        searchService.displaySearchResults(electronicsResults);

        // Calculator (102) and Laptop (103) are AVAILABLE; Earbuds (106) is CLAIM_PENDING
        boolean hasCalcElec = electronicsResults.stream().anyMatch(i -> i.getItemId() == 102);
        boolean hasLaptopElec = electronicsResults.stream().anyMatch(i -> i.getItemId() == 103);
        boolean hasEarbudsElec = electronicsResults.stream().anyMatch(i -> i.getItemId() == 106);

        if (electronicsResults.size() == 2 && hasCalcElec && hasLaptopElec && !hasEarbudsElec) {
            System.out.println("  PASS: Found 2 available electronics items (non-available items excluded).");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Electronics category search failed.");
        }

        // Test searchByCategory by string ("electronics")
        ArrayList<Item> elecByString = searchService.searchByCategory("electronics");
        if (elecByString.size() == 2) {
            System.out.println("  PASS: Category search by string 'electronics' passed.");
        } else {
            System.err.println("  FAIL: Category search by string failed.");
        }

        // -------------------------------------------------------------
        // TEST 4: Searching for a nonexistent item
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 4] Searching for a nonexistent item ('Umbrella')");
        ArrayList<Item> nonexistentResults = searchService.searchByName("Umbrella");
        searchService.displaySearchResults(nonexistentResults);

        if (nonexistentResults.isEmpty()) {
            System.out.println("  PASS: Correctly returned empty list and handled display gracefully.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Nonexistent item returned unexpected results.");
        }

        // -------------------------------------------------------------
        // TEST 5: Verify CLAIMED items are NEVER displayed (Item 105)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 5] Verify CLAIMED items are NEVER displayed in search results");
        ArrayList<Item> watchSearch = searchService.searchByName("Watch");
        Item watchById = searchService.searchById(105);

        if (watchSearch.isEmpty() && watchById == null) {
            System.out.println("  PASS: Claimed watch (ID 105) excluded from searchByName and searchById.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Claimed item was returned in search results.");
        }

        // -------------------------------------------------------------
        // TEST 6: Generic Searchable<Item> polymorphism & searchById()
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 6] Searchable<Item> interface polymorphism & searchById()");
        Searchable<Item> searchableInterface = searchService;
        Item foundItem = searchableInterface.searchById(103);

        if (foundItem != null && foundItem.getName().equals("Dell Inspiron 15 Laptop")) {
            System.out.println("  PASS: Searchable<Item>.searchById(103) returned correct item.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: searchById failed.");
        }

        // -------------------------------------------------------------
        // TEST 7: Keyword Search across description & location
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 7] Keyword search ('silicon' / 'Hilfiger' / 'Sophomore')");
        ArrayList<Item> keywordResults = searchService.searchByKeyword("Hilfiger");
        if (keywordResults.size() == 1 && keywordResults.get(0).getItemId() == 101) {
            System.out.println("  PASS: Keyword matching description found wallet.");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Keyword search failed.");
        }

        // -------------------------------------------------------------
        // TEST 8: Multi-criteria filter (Location AND Category)
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("\n[TEST 8] Combined Location and Category filter");
        ArrayList<Item> combinedResults = searchService.searchByLocationAndCategory("Library", ItemCategory.ELECTRONICS);
        if (combinedResults.size() == 1 && combinedResults.get(0).getItemId() == 102) {
            System.out.println("  PASS: Combined filter returned only Casio Calculator (Library + Electronics).");
            testsPassed++;
        } else {
            System.err.println("  FAIL: Combined filter failed.");
        }

        // Summary
        System.out.println("\n================================================================================");
        System.out.println("TEST SUMMARY: " + testsPassed + "/" + totalTests + " test suites PASSED.");
        System.out.println("================================================================================");

        if (testsPassed != totalTests) {
            System.exit(1);
        }
    }
}
