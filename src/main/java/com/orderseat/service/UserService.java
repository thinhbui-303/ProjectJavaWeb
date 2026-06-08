package com.orderseat.service;

import com.orderseat.entity.User;

public interface UserService {
    void registerNewCustomer(User user);
    User findByUsername(String username);
    void updateProfile(String username, User updatedData);
    java.util.List<User> getAllUsers();
    User getUserById(Long id);
    void updateUserRole(Long userId, String roleName, String currentAdminUsername);
    void deleteUser(Long userId, String currentAdminUsername);
}
