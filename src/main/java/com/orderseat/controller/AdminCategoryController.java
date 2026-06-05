package com.orderseat.controller;

import com.orderseat.entity.Category;
import com.orderseat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("parentCategories", categoryService.getTopLevelCategories());
        return "admin/category_form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam(value = "parentId", required = false) Long parentId) {
        if (parentId != null) {
            Category parent = categoryService.getCategoryById(parentId);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }
        categoryService.saveCategory(category);
        return "redirect:/admin/categories?success=true";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        
        // Parent candidates should be top-level categories, excluding current category
        java.util.List<Category> parents = categoryService.getTopLevelCategories().stream()
                .filter(c -> !c.getId().equals(id))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("parentCategories", parents);
        return "admin/category_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return "redirect:/admin/categories?deleted=true";
        } catch (Exception e) {
            return "redirect:/admin/categories?error=conflict";
        }
    }
}
