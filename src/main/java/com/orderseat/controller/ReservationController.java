package com.orderseat.controller;

import com.orderseat.dto.CartItem;
import com.orderseat.entity.Reservation;
import com.orderseat.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @SuppressWarnings("unchecked")
    @GetMapping("/booking")
    public String showBookingForm(Model model, HttpSession session) {
        model.addAttribute("tables", reservationService.getAvailableTables());
        model.addAttribute("reservation", new Reservation());

        // Lấy giỏ hàng từ session để hiển thị tóm tắt trên trang booking
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        model.addAttribute("cartItems", cart != null ? cart.values() : Collections.emptyList());
        double cartTotal = cart != null
                ? cart.values().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum()
                : 0.0;
        model.addAttribute("cartTotal", cartTotal);

        return "booking";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/booking")
    public String processBooking(@ModelAttribute Reservation reservation,
                                 Principal principal,
                                 HttpSession session,
                                 org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");

        try {
            reservationService.createReservation(reservation, username, cart);
            // Xóa giỏ hàng sau khi đặt bàn thành công
            session.removeAttribute("cart");
            return "redirect:/customer/history?success=true";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/customer/booking";
        }
    }

    @GetMapping("/booking/cancel/{id}")
    public String cancelBooking(@PathVariable("id") Long id, Principal principal) {
        try {
            reservationService.cancelReservation(id, principal.getName());
            return "redirect:/customer/history?cancelSuccess=true";
        } catch (Exception e) {
            return "redirect:/customer/history?cancelError=" + e.getMessage();
        }
    }

    @GetMapping("/history")
    public String showHistory(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("reservations", reservationService.getHistoryByUsername(username));
        return "history";
    }
}
