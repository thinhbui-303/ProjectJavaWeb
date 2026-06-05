package com.orderseat.service;

import com.orderseat.entity.CoffeeTable;
import com.orderseat.entity.enums.TableStatus;

import java.util.List;

public interface CoffeeTableService {
    List<CoffeeTable> getAllTables();
    CoffeeTable getTableById(Long id);
    void saveTable(CoffeeTable table);
    void deleteTable(Long id);
    void updateTableStatus(Long id, TableStatus status);
}
