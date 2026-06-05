package com.orderseat.controller;

import com.orderseat.entity.enums.ReservationStatus;
import com.orderseat.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("statuses", ReservationStatus.values());
        return "admin/reservations";
    }

    @PostMapping("/update-status/{id}")
    public String updateReservationStatus(@PathVariable("id") Long id,
                                          @RequestParam("status") ReservationStatus status) {
        reservationService.updateStatus(id, status);
        return "redirect:/admin/reservations?success=true";
    }
}
