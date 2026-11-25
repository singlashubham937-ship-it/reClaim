package util;

import model.*;
import repository.*;

import java.io.*;
import java.util.ArrayList;

/**
 * FileStorageManager saves and loads application data to/from text files.
 *
 * File format: simple pipe-delimited (|) CSV lines. Each record type is
 * stored in its own file inside the "data/" folder relative to where
 * the application runs.
 *
 * Files created:
 *   data/users.txt       – User records
 *   data/items.txt       – Item records
 *   data/claims.txt      – Claim records
 *   data/handovers.txt   – Handover records
 *   data/lostreports.txt – LostReport records
 *
 * Important:
 *   - Passwords are stored as plain text for this educational demo.
 *   - If a file is missing, the load methods do nothing (empty repo stays empty).
 *   - File errors are caught and printed without crashing the app.
 *
 * OOP concept demonstrated: File I/O (BufferedReader, FileWriter), exception handling
 */
public class FileStorageManager {

    // Folder where all data files are stored
    private static final String DATA_DIR   = "data";
    private static final String USERS_FILE     = DATA_DIR + "/users.txt";
    private static final String ITEMS_FILE     = DATA_DIR + "/items.txt";
    private static final String CLAIMS_FILE    = DATA_DIR + "/claims.txt";
    private static final String HANDOVERS_FILE = DATA_DIR + "/handovers.txt";
    private static final String LOST_REPORTS_FILE = DATA_DIR + "/lostreports.txt";

    // The repositories whose data we save and load
    private UserRepository       userRepository;
    private ItemRepository       itemRepository;
    private ClaimRepository      claimRepository;
    private HandoverRepository   handoverRepository;
    private LostReportRepository lostReportRepository;

    /**
     * Constructor – receives every repository so we can persist all data.
     */
    public FileStorageManager(UserRepository userRepository,
                              ItemRepository itemRepository,
                              ClaimRepository claimRepository,
                              HandoverRepository handoverRepository,
                              LostReportRepository lostReportRepository) {
        this.userRepository       = userRepository;
        this.itemRepository       = itemRepository;
        this.claimRepository      = claimRepository;
        this.handoverRepository   = handoverRepository;
        this.lostReportRepository = lostReportRepository;
    }

    // ================================================================
    // SAVE ALL DATA
    // ================================================================

    /**
     * Saves all data from every repository to files.
     * Call this when the application exits.
     */
    public void saveAll() {
        ensureDataDirExists();
        saveUsers();
        saveItems();
        saveClaims();
        saveHandovers();
        saveLostReports();
        System.out.println("All data saved successfully.");
    }

    // ================================================================
    // LOAD ALL DATA
    // ================================================================

    /**
     * Loads all data from files into the repositories.
     * Call this when the application starts.
     */
    public void loadAll() {
        loadUsers();
        loadItems();
        loadClaims();
        loadHandovers();
        loadLostReports();
        System.out.println("Data loaded from files.");
    }

    // ================================================================
    // Private helper – create the data folder if it doesn't exist
    // ================================================================

    private void ensureDataDirExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ================================================================
    // USERS
    // Format: userId|name|email|password|role
    // ================================================================

    private void saveUsers() {
        try (FileWriter fw = new FileWriter(USERS_FILE)) {
            for (User user : userRepository.getAllUsers()) {
                fw.write(user.getUserId() + "|" +
                         user.getName()   + "|" +
                         user.getEmail()  + "|" +
                         user.getPassword() + "|" +
                         user.getRole().name() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue;
                String   userId   = parts[0];
                String   name     = parts[1];
                String   email    = parts[2];
                String   password = parts[3];
                UserRole role     = UserRole.valueOf(parts[4]);
                User user = new User(userId, name, email, password, role);
                userRepository.addUser(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // ================================================================
    // ITEMS
    // Format: itemId|title|description|finderId|location|status|foundDate
    // ================================================================

    private void saveItems() {
        try (FileWriter fw = new FileWriter(ITEMS_FILE)) {
            for (Item item : itemRepository.getAllItems()) {
                fw.write(item.getItemId()      + "|" +
                         item.getTitle()       + "|" +
                         item.getDescription() + "|" +
                         item.getFinderId()    + "|" +
                         item.getLocation()    + "|" +
                         item.getStatus().name() + "|" +
                         item.getFoundDate()   + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving items: " + e.getMessage());
        }
    }

    private void loadItems() {
        File file = new File(ITEMS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;
                Item item = new Item(
                        parts[0], parts[1], parts[2], parts[3],
                        parts[4], ItemStatus.valueOf(parts[5]), parts[6]);
                itemRepository.addItem(item);
            }
        } catch (IOException e) {
            System.out.println("Error loading items: " + e.getMessage());
        }
    }

    // ================================================================
    // CLAIMS
    // Format: claimId|itemId|claimantId|status|remarks
    // ================================================================

    private void saveClaims() {
        try (FileWriter fw = new FileWriter(CLAIMS_FILE)) {
            for (Claim claim : claimRepository.getAllClaims()) {
                fw.write(claim.getClaimId()    + "|" +
                         claim.getItemId()     + "|" +
                         claim.getClaimantId() + "|" +
                         claim.getStatus().name() + "|" +
                         claim.getRemarks()    + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving claims: " + e.getMessage());
        }
    }

    private void loadClaims() {
        File file = new File(CLAIMS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue;
                Claim claim = new Claim(
                        parts[0], parts[1], parts[2],
                        ClaimStatus.valueOf(parts[3]), parts[4]);
                claimRepository.addClaim(claim);
            }
        } catch (IOException e) {
            System.out.println("Error loading claims: " + e.getMessage());
        }
    }

    // ================================================================
    // HANDOVERS
    // Format: handoverId|itemId|claimId|finderId|ownerId|location|remarks|confirmed|date
    // ================================================================

    private void saveHandovers() {
        try (FileWriter fw = new FileWriter(HANDOVERS_FILE)) {
            for (Handover h : handoverRepository.getAllHandovers()) {
                String date = (h.getHandoverDate() != null) ? h.getHandoverDate().toString() : "";
                fw.write(h.getHandoverId()       + "|" +
                         h.getItemId()           + "|" +
                         h.getClaimId()          + "|" +
                         h.getFinderId()         + "|" +
                         h.getOwnerId()          + "|" +
                         h.getHandoverLocation() + "|" +
                         h.getRemarks()          + "|" +
                         h.isConfirmed()         + "|" +
                         date + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving handovers: " + e.getMessage());
        }
    }

    private void loadHandovers() {
        File file = new File(HANDOVERS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 9) continue;
                boolean confirmed = Boolean.parseBoolean(parts[7]);
                java.time.LocalDateTime date = null;
                if (!parts[8].isEmpty()) {
                    try { date = java.time.LocalDateTime.parse(parts[8]); }
                    catch (Exception ignored) {}
                }
                Handover h = new Handover(
                        parts[0], parts[1], parts[2],
                        parts[3], parts[4], date,
                        parts[5], parts[6], confirmed);
                handoverRepository.addHandover(h);
            }
        } catch (IOException e) {
            System.out.println("Error loading handovers: " + e.getMessage());
        }
    }

    // ================================================================
    // LOST REPORTS
    // Format: reportId|reportedByUserId|itemName|description|location|reportDate
    // ================================================================

    private void saveLostReports() {
        try (FileWriter fw = new FileWriter(LOST_REPORTS_FILE)) {
            for (LostReport r : lostReportRepository.getAllLostReports()) {
                fw.write(r.getReportId()          + "|" +
                         r.getReportedByUserId()  + "|" +
                         r.getItemName()          + "|" +
                         r.getDescription()       + "|" +
                         r.getLocation()          + "|" +
                         r.getReportDate()        + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving lost reports: " + e.getMessage());
        }
    }

    private void loadLostReports() {
        File file = new File(LOST_REPORTS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                LostReport r = new LostReport(
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                lostReportRepository.addLostReport(r);
            }
        } catch (IOException e) {
            System.out.println("Error loading lost reports: " + e.getMessage());
        }
    }
}
