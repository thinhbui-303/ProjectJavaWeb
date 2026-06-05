package com.orderseat.service.impl;

import com.orderseat.entity.CoffeeTable;
import com.orderseat.entity.enums.TableStatus;
import com.orderseat.repository.CoffeeTableRepository;
import com.orderseat.service.CoffeeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeTableServiceImpl implements CoffeeTableService {

    private final CoffeeTableRepository coffeeTableRepository;

    @Override
    public List<CoffeeTable> getAllTables() {
        return coffeeTableRepository.findAll();
    }

    @Override
    public CoffeeTable getTableById(Long id) {
        return coffeeTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn có ID: " + id));
    }

    @Override
    public void saveTable(CoffeeTable table) {
        coffeeTableRepository.save(table);
    }

    @Override
    public void deleteTable(Long id) {
        try {
            coffeeTableRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Không thể xóa do bàn đã có dữ liệu đặt bàn. Vui lòng chuyển trạng thái sang BẢO TRÌ.");
        }
    }

    @Override
    public void updateTableStatus(Long id, TableStatus status) {
        CoffeeTable table = getTableById(id);
        table.setStatus(status);
        coffeeTableRepository.save(table);
    }
}
