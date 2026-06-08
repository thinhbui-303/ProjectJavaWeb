package com.orderseat.controller;

import com.orderseat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUsername", principal.getName());
        return "admin/users";
    }

    @PostMapping("/roles")
    public String updateUserRole(@RequestParam("userId") Long userId,
                                 @RequestParam("roleName") String roleName,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(userId, roleName, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật quyền thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userId, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
