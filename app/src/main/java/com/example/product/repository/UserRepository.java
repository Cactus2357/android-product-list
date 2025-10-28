package com.example.product.repository;

import android.content.Context;

import com.example.product.AppDatabase;
import com.example.product.entity.User;

public class UserRepository {
    private final AppDatabase db;

    public UserRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public boolean register(String username, String password, String email) {
        if (db.userDao().getUserByUsername(username) != null) return false;
        db.userDao().insert(new User(username, password, email));
        return true;
    }

    public boolean login(String username, String password) {
        return db.userDao().login(username, password) != null;
    }
}
