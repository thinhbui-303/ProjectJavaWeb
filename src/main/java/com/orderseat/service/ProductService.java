package com.orderseat.service;

import com.orderseat.dto.ProductRequest;
import com.orderseat.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    java.util.List<Product> getProductsByCategory(Long categoryId);
    org.springframework.data.domain.Page<Product> getProductsPaged(String keyword, int page, int size);
    org.springframework.data.domain.Page<Product> getProductsByCategoryPaged(Long categoryId, String keyword, int page, int size);
    void saveProduct(ProductRequest productRequest);
    void deleteProduct(Long id);
}
