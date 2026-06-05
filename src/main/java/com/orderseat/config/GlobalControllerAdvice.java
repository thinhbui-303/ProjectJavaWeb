package com.orderseat.config;

import com.orderseat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
    }
}
