package com.reclaim.model;

import com.reclaim.enums.UserRole;

public class Student extends User {

    public Student(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, UserRole.STUDENT);
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}
