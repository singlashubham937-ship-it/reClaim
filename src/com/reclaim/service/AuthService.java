package com.reclaim.service;

import com.reclaim.model.Finder;
import com.reclaim.model.Student;
import com.reclaim.model.User;
import com.reclaim.repository.UserRepository;

public class AuthService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new Student
    public boolean registerStudent(int userId, String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Registration failed: Name cannot be empty.");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("Registration failed: Invalid email address.");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Registration failed: Password cannot be empty.");
            return false;
        }
        if (isEmailRegistered(email)) {
            System.out.println("Registration failed: Email is already registered.");
            return false;
        }

        Student student = new Student(userId, name, email, phone, password);
        boolean added = userRepository.addUser(student);

        if (added) {
            System.out.println("Student registered successfully: " + name);
        } else {
            System.out.println("Registration failed: User ID already exists.");
        }
        return added;
    }

    // Register a new Finder
    public boolean registerFinder(int userId, String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Registration failed: Name cannot be empty.");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("Registration failed: Invalid email address.");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Registration failed: Password cannot be empty.");
            return false;
        }
        if (isEmailRegistered(email)) {
            System.out.println("Registration failed: Email is already registered.");
            return false;
        }

        Finder finder = new Finder(userId, name, email, phone, password);
        boolean added = userRepository.addUser(finder);

        if (added) {
            System.out.println("Finder registered successfully: " + name);
        } else {
            System.out.println("Registration failed: User ID already exists.");
        }
        return added;
    }

    // Login using email and password
    public User login(String email, String password) {
        if (email == null || password == null) {
            System.out.println("Login failed: Email or password cannot be null.");
            return null;
        }

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            System.out.println("Login failed: No account found with this email.");
            return null;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("Login failed: Incorrect password.");
            return null;
        }

        System.out.println("Login successful! Welcome, " + user.getName() + " (" + user.getUserType() + ")");
        return user;
    }

    // Check if an email is already registered
    public boolean isEmailRegistered(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    // Get the logged-in user by email (useful after login)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
