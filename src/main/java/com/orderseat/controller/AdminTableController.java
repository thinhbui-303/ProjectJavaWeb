package com.orderseat.controller;

import com.orderseat.entity.CoffeeTable;
import com.orderseat.entity.enums.TableStatus;
import com.orderseat.service.CoffeeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tables")
@RequiredArgsConstructor
public class AdminTableController {

    private final CoffeeTableService coffeeTableService;

    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", coffeeTableService.getAllTables());
        return "admin/tables";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("coffeeTable", new CoffeeTable());
        return "admin/table_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        CoffeeTable table = coffeeTableService.getTableById(id);
        model.addAttribute("coffeeTable", table);
        return "admin/table_form";
    }

    @PostMapping("/save")
    public String saveTable(@ModelAttribute CoffeeTable coffeeTable, RedirectAttributes redirectAttributes) {
        try {
            coffeeTableService.saveTable(coffeeTable);
            redirectAttributes.addFlashAttribute("success", "Đã lưu thông tin bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu bàn: " + e.getMessage());
        }
        return "redirect:/admin/tables";
    }

    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            coffeeTableService.deleteTable(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/tables";
    }

    @GetMapping("/status/{id}")
    public String updateStatus(@PathVariable("id") Long id, @RequestParam("status") TableStatus status, RedirectAttributes redirectAttributes) {
        try {
            coffeeTableService.updateTableStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật trạng thái bàn!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/tables";
    }
}
