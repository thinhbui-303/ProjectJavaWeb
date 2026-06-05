package com.orderseat.service.impl;

import com.orderseat.entity.Role;
import com.orderseat.entity.User;
import com.orderseat.repository.RoleRepository;
import com.orderseat.repository.UserRepository;
import com.orderseat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerNewCustomer(User user) {
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Cấp quyền CUSTOMER mặc định (ROLE_CUSTOMER phải tồn tại trong DB)
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy ROLE_CUSTOMER"));
        user.setRoles(Set.of(customerRole));

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void updateProfile(String username, User updatedData) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        user.setFullName(updatedData.getFullName());
        user.setPhone(updatedData.getPhone());
        userRepository.save(user);
    }
}
