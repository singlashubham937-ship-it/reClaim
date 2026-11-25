package model;

/**
 * Represents an Admin user in the reClaim system.
 *
 * Admin extends User and always has the role ADMIN.
 * The admin can view and manage all application data.
 *
 * OOP concept demonstrated: Inheritance (Admin extends User)
 */
public class Admin extends User {

    /**
     * Creates an Admin user.
     *
     * @param userId   Unique identifier for this admin
     * @param name     Full name
     * @param email    Email address
     * @param password Password (stored as plain text for this demo)
     */
    public Admin(String userId, String name, String email, String password) {
        // Call the parent (User) constructor and hard-code the role as ADMIN
        super(userId, name, email, password, UserRole.ADMIN);
    }

    /**
     * Returns a readable string for this Admin.
     */
    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role=" + getRole() +
                '}';
    }
}
