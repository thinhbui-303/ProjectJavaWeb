package com.orderseat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final com.orderseat.service.StatisticsService statisticsService;

    public HomeController(com.orderseat.service.StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(org.springframework.ui.Model model,
                                 @org.springframework.web.bind.annotation.RequestParam(value = "forbidden", required = false) String forbidden) {
        model.addAttribute("totalReservations", statisticsService.getTotalReservations());
        model.addAttribute("totalRevenue", statisticsService.getTotalRevenue());
        model.addAttribute("totalCustomers", statisticsService.getTotalCustomers());
        model.addAttribute("topProducts", statisticsService.getTopSellingProducts());
        if (forbidden != null) {
            model.addAttribute("forbiddenWarning", "Tài khoản Admin không có quyền truy cập trang của khách hàng.");
        }
        return "admin_dashboard";
    }
}
