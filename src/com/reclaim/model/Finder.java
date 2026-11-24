package com.reclaim.model;

import com.reclaim.enums.UserRole;

public class Finder extends User {

    public Finder(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, UserRole.FINDER);
    }

    @Override
    public String getUserType() {
        return "Finder";
    }
}
