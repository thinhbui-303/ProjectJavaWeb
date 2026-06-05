package com.orderseat.controller;

import com.orderseat.entity.User;
import com.orderseat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, Principal principal) {
        userService.updateProfile(principal.getName(), updatedUser);
        return "redirect:/customer/profile?success=true";
    }
}
