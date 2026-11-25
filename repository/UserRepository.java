package repository;

import model.User;
import model.UserRole;
import java.util.ArrayList;

/**
 * Stores all User objects (Students, Finders, and Admins) in an ArrayList.
 *
 * We use ONE repository for all user types because Admin extends User.
 * No separate AdminRepository is needed.
 *
 * OOP concept demonstrated: Polymorphism (storing Admin objects as User references)
 */
public class UserRepository {

    // All users (of any role) live in this single list
    private ArrayList<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    /**
     * Adds a user. Returns false if a user with the same ID already exists.
     */
    public boolean addUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }
        if (getUserById(user.getUserId()) != null) {
            System.out.println("User ID already exists: " + user.getUserId());
            return false;
        }
        users.add(user);
        return true;
    }

    /**
     * Finds a user by their unique ID.
     * Returns null if not found.
     */
    public User getUserById(String userId) {
        if (userId == null) return null;
        for (User u : users) {
            if (userId.equals(u.getUserId())) {
                return u;
            }
        }
        return null;
    }

    /**
     * Finds a user by their email address.
     * Used during login.
     */
    public User getUserByEmail(String email) {
        if (email == null) return null;
        for (User u : users) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return u;
            }
        }
        return null;
    }

    /**
     * Returns every user in the system.
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Returns only users with a specific role (e.g., all STUDENTs or all FINDERs).
     */
    public ArrayList<User> getUsersByRole(UserRole role) {
        ArrayList<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getRole() == role) {
                result.add(u);
            }
        }
        return result;
    }

    /**
     * Returns the total number of registered users.
     */
    public int getTotalUserCount() {
        return users.size();
    }
}
