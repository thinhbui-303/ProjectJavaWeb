package com.orderseat.controller;

import com.orderseat.entity.Product;
import com.orderseat.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MenuController {
    
    private final ProductService productService;
    private final com.orderseat.service.CategoryService categoryService;

    @GetMapping("/menu")
    public String menu(@RequestParam(value = "catId", required = false) Long catId,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {
        
        // 8 sản phẩm 1 trang
        int size = 8;
        Page<Product> productPage;
        com.orderseat.entity.Category selectedCategory = null;
        com.orderseat.entity.Category parentCategory = null;
        
        if (catId != null) {
            selectedCategory = categoryService.getCategoryById(catId);
            if (selectedCategory.getParent() != null) {
                parentCategory = selectedCategory.getParent();
            } else {
                parentCategory = selectedCategory;
            }
            productPage = productService.getProductsByCategoryPaged(catId, keyword, page, size);
        } else {
            productPage = productService.getProductsPaged(keyword, page, size);
        }
        
        model.addAttribute("productPage", productPage);
        model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
        model.addAttribute("selectedCategory", selectedCategory);
        model.addAttribute("parentCategory", parentCategory);
        model.addAttribute("selectedCatId", catId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        
        return "menu"; 
    }
}
