package com.orderseat.repository;

import com.orderseat.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    java.util.List<Category> findByParentIsNull();
}
