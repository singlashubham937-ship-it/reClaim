package com.reclaim.model;

import com.reclaim.enums.UserRole;

public class Admin extends User {

    public Admin(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, UserRole.ADMIN);
    }

    @Override
    public String getUserType() {
        return "Admin";
    }
}
