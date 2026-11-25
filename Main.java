import model.*;
import repository.*;
import service.*;
import ui.MainMenu;
import util.FileStorageManager;

import java.util.Scanner;

/**
 * Main.java – the entry point of the reClaim application.
 *
 * Responsibilities:
 *  1. Create all repositories (ONCE – shared across the application).
 *  2. Create all services (inject repositories via constructors).
 *  3. Load any previously saved data.
 *  4. Start the main menu.
 *  5. Save all data when the user exits.
 *
 * OOP concept demonstrated: Dependency injection, single responsibility,
 *                            proper initialisation order.
 */
public class Main {

    public static void main(String[] args) {

        // ---- 1. Create repositories (the in-memory data stores) ----
        UserRepository       userRepository       = new UserRepository();
        ItemRepository       itemRepository       = new ItemRepository();
        ClaimRepository      claimRepository      = new ClaimRepository();
        HandoverRepository   handoverRepository   = new HandoverRepository();
        LostReportRepository lostReportRepository = new LostReportRepository();

        // ---- 2. Create services (inject repositories) ----
        AuthService       authService       = new AuthService(userRepository);
        ItemService       itemService       = new ItemService(itemRepository);
        ClaimService      claimService      = new ClaimService(claimRepository, itemRepository);
        HandoverService   handoverService   = new HandoverService(handoverRepository, itemRepository, claimRepository);
        LostReportService lostReportService = new LostReportService(lostReportRepository);
        AdminService      adminService      = new AdminService(
                                                  userRepository, itemRepository,
                                                  lostReportRepository, claimRepository,
                                                  handoverRepository);

        // ---- 3. File persistence: load saved data ----
        FileStorageManager storage = new FileStorageManager(
                userRepository, itemRepository,
                claimRepository, handoverRepository, lostReportRepository);
        storage.loadAll();

        // Create a default admin account if no users are registered yet
        // (so the application is usable right away on first run)
        if (userRepository.getAllUsers().isEmpty()) {
            Admin defaultAdmin = new Admin("ADM-1", "System Admin", "admin@reclaim.edu", "admin123");
            userRepository.addUser(defaultAdmin);
            System.out.println("Default admin created: email=admin@reclaim.edu  password=admin123");
        }

        // ---- 4. Create a shared Scanner (never closed inside menus) ----
        Scanner scanner = new Scanner(System.in);

        // ---- 5. Start the main menu ----
        MainMenu mainMenu = new MainMenu(
                authService, itemService, claimService,
                handoverService, lostReportService, adminService, scanner);
        mainMenu.start();

        // ---- 6. Save all data before exiting ----
        storage.saveAll();
        scanner.close();
    }
}
