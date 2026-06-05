package com.orderseat.repository;

import com.orderseat.entity.CoffeeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeTableRepository extends JpaRepository<CoffeeTable, Long> {
}
