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

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void updateUserRole(Long userId, String roleName, String currentAdminUsername) {
        User user = getUserById(userId);
        
        if (user.getUsername().equals(currentAdminUsername)) {
            throw new RuntimeException("Bạn không thể tự thay đổi quyền của chính mình!");
        }

        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));

        user.getRoles().clear();
        user.getRoles().add(newRole);
        userRepository.save(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteUser(Long userId, String currentAdminUsername) {
        User user = getUserById(userId);
        
        if (user.getUsername().equals(currentAdminUsername)) {
            throw new RuntimeException("Bạn không thể tự xóa tài khoản của chính mình!");
        }
        
        userRepository.deleteById(userId);
    }
}
