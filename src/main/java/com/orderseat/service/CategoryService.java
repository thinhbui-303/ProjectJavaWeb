package com.orderseat.service;

import com.orderseat.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getTopLevelCategories();
    Category getCategoryById(Long id);
    void saveCategory(Category category);
    void deleteCategory(Long id);
}
