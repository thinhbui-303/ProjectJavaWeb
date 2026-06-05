package com.orderseat.repository;

import com.orderseat.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findByCategoryId(Long categoryId);
    java.util.List<Product> findByCategoryIdIn(java.util.Collection<Long> categoryIds);
}
