package com.orderseat.service;

import com.orderseat.dto.ProductRequest;
import com.orderseat.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    java.util.List<Product> getProductsByCategory(Long categoryId);
    void saveProduct(ProductRequest productRequest);
    void deleteProduct(Long id);
}
