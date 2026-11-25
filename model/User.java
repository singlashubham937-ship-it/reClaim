package model;

/**
 * Represents a generic user in the reClaim system.
 *
 * This class holds the common fields shared by all user types
 * (Student, Finder, Admin). The 'role' field lets the application
 * identify what kind of user this is.
 *
 * OOP concept demonstrated: Encapsulation (private fields with getters/setters)
 */
public class User {

    // Private fields – encapsulation principle
    private String userId;
    private String name;
    private String email;
    private String password;
    private UserRole role;   // e.g. STUDENT, FINDER, ADMIN

    /**
     * Full constructor – used when creating a new user.
     */
    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ---- Getters & Setters ----

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    /**
     * Returns true if this user is an admin.
     * Convenience method used by admin-only features.
     */
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
