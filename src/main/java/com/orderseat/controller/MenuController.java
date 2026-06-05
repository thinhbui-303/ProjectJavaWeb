package com.orderseat.controller;

import com.orderseat.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {
    
    private final ProductService productService;
    private final com.orderseat.service.CategoryService categoryService;

    @GetMapping("/menu")
    public String menu(@org.springframework.web.bind.annotation.RequestParam(value = "catId", required = false) Long catId, 
                       Model model) {
        java.util.List<com.orderseat.entity.Product> products;
        com.orderseat.entity.Category selectedCategory = null;
        com.orderseat.entity.Category parentCategory = null;
        
        if (catId != null) {
            selectedCategory = categoryService.getCategoryById(catId);
            if (selectedCategory.getParent() != null) {
                parentCategory = selectedCategory.getParent();
            } else {
                parentCategory = selectedCategory;
            }
            products = productService.getProductsByCategory(catId);
        } else {
            products = productService.getAllProducts();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
        model.addAttribute("selectedCategory", selectedCategory);
        model.addAttribute("parentCategory", parentCategory);
        model.addAttribute("selectedCatId", catId);
        return "menu"; 
    }
}
