package com.orderseat.controller;

import com.orderseat.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final com.orderseat.service.CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products"; 
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new com.orderseat.dto.ProductRequest());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product_form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute com.orderseat.dto.ProductRequest productRequest) {
        productService.saveProduct(productRequest);
        return "redirect:/admin/products?success=true";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        com.orderseat.entity.Product product = productService.getProductById(id);
        // Map Entity to DTO
        com.orderseat.dto.ProductRequest request = com.orderseat.dto.ProductRequest.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
                
        model.addAttribute("product", request);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products?deleted=true";
    }
}
