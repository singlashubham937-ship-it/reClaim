package ui;

import model.*;
import service.*;
import util.InputHandler;

import java.util.Scanner;

/**
 * MainMenu is the first screen the user sees when they start reClaim.
 *
 * Options: Register, Login, Exit.
 * After a successful login the user is routed to their role-specific menu.
 *
 * OOP concept demonstrated: Encapsulation, role-based dispatch (polymorphism in routing)
 */
public class MainMenu {

    // All services passed in – created ONCE in Main.java and shared here
    private AuthService       authService;
    private ItemService       itemService;
    private ClaimService      claimService;
    private HandoverService   handoverService;
    private LostReportService lostReportService;
    private AdminService      adminService;
    private Scanner           scanner;
    private InputHandler      input;

    public MainMenu(AuthService authService,
                    ItemService itemService,
                    ClaimService claimService,
                    HandoverService handoverService,
                    LostReportService lostReportService,
                    AdminService adminService,
                    Scanner scanner) {
        this.authService       = authService;
        this.itemService       = itemService;
        this.claimService      = claimService;
        this.handoverService   = handoverService;
        this.lostReportService = lostReportService;
        this.adminService      = adminService;
        this.scanner           = scanner;
        this.input             = new InputHandler(scanner);
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   reClaim – Lost & Found System      ║");
        System.out.println("║   University Campus Edition           ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("================================");

            int choice = input.readIntInRange("Enter choice: ", 1, 3);

            switch (choice) {

                case 1:
                    // Registration
                    handleRegister();
                    break;

                case 2:
                    // Login
                    User loggedIn = handleLogin();
                    if (loggedIn != null) {
                        routeToMenu(loggedIn);
                    }
                    break;

                case 3:
                    System.out.println("Thank you for using reClaim. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Handles the registration flow.
     */
    private void handleRegister() {
        System.out.println("\n--- Register ---");
        System.out.println("Select your role:");
        System.out.println("1. Student (can browse and claim items)");
        System.out.println("2. Finder (can report found items)");

        int roleChoice = input.readIntInRange("Enter role: ", 1, 2);
        UserRole role = (roleChoice == 1) ? UserRole.STUDENT : UserRole.FINDER;

        String name     = input.readString("Full name: ");
        String email    = input.readString("Email: ");
        String password = input.readString("Password: ");

        authService.register(name, email, password, role);
    }

    /**
     * Handles the login flow. Returns the logged-in User, or null on failure.
     */
    private User handleLogin() {
        System.out.println("\n--- Login ---");
        String email    = input.readString("Email: ");
        String password = input.readString("Password: ");
        return authService.login(email, password);
    }

    /**
     * Routes the logged-in user to the correct menu based on their role.
     * This is the polymorphic dispatch point.
     */
    private void routeToMenu(User user) {
        UserRole role = user.getRole();

        if (role == UserRole.STUDENT) {
            StudentMenu.show(user, itemService, claimService, lostReportService, scanner);

        } else if (role == UserRole.FINDER) {
            FinderMenu.show(user, itemService, claimService, handoverService, scanner);

        } else if (role == UserRole.ADMIN) {
            AdminMenu.show(user, adminService, scanner);

        } else {
            System.out.println("Unknown role. Cannot open menu.");
        }
    }
}
