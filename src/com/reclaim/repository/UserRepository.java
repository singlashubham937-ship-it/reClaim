package com.reclaim.repository;

import com.reclaim.model.User;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        if (getUserById(user.getUserId()) != null) {
            return false;
        }
        if (findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        users.add(user);
        return true;
    }

    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User findUserByEmail(String email) {
        if (email == null) {
            return null;
        }
        for (User user : users) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    public boolean removeUser(int userId) {
        User user = getUserById(userId);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
}
