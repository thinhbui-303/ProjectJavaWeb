package com.orderseat.service;

import com.orderseat.entity.User;

public interface UserService {
    void registerNewCustomer(User user);
    User findByUsername(String username);
    void updateProfile(String username, User updatedData);
}
