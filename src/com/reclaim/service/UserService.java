package com.reclaim.service;

import com.reclaim.model.User;
import com.reclaim.repository.UserRepository;
import java.util.ArrayList;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add a user (checks for duplicates via repository)
    public boolean addUser(User user) {
        if (user == null) {
            System.out.println("Cannot add a null user.");
            return false;
        }
        boolean added = userRepository.addUser(user);
        if (!added) {
            System.out.println("User could not be added. ID or email already exists.");
        }
        return added;
    }

    // Find a user by their ID
    public User getUserById(int userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            System.out.println("No user found with ID: " + userId);
        }
        return user;
    }

    // Find a user by their email
    public User getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
        }
        return user;
    }

    // Get all registered users
    public ArrayList<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    // Remove a user by ID (admin use)
    public boolean removeUser(int userId) {
        boolean removed = userRepository.removeUser(userId);
        if (removed) {
            System.out.println("User with ID " + userId + " removed successfully.");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
        return removed;
    }
}
