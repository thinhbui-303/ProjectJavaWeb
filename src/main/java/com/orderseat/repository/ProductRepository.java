package com.orderseat.repository;

import com.orderseat.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findByCategoryId(Long categoryId);
    java.util.List<Product> findByCategoryIdIn(Collection<Long> categoryIds);
    
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryIdIn(Collection<Long> categoryIds, Pageable pageable);
    Page<Product> findByCategoryIdInAndNameContainingIgnoreCase(Collection<Long> categoryIds, String name, Pageable pageable);
}
