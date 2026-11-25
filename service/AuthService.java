package service;

import model.*;
import repository.*;
import util.DateTimeUtil;
import util.IdGenerator;

import java.util.ArrayList;

/**
 * AuthService handles user registration and login for the reClaim application.
 *
 * OOP concept demonstrated: Encapsulation, service layer
 */
public class AuthService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user (Student or Finder).
     * Returns the created User, or null if the email is already registered.
     */
    public User register(String name, String email, String password, UserRole role) {
        // Check email not already used
        if (userRepository.getUserByEmail(email) != null) {
            System.out.println("An account with this email already exists.");
            return null;
        }
        String userId = IdGenerator.nextUserId();
        User user = new User(userId, name, email, password, role);
        userRepository.addUser(user);
        System.out.println("Registration successful! Your User ID is: " + userId);
        return user;
    }

    /**
     * Logs in a user by email and password.
     * Returns the User if credentials match, or null otherwise.
     */
    public User login(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            System.out.println("No account found for email: " + email);
            return null;
        }
        if (!user.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            return null;
        }
        System.out.println("Login successful! Welcome, " + user.getName() + " [" + user.getRole() + "]");
        return user;
    }
}
