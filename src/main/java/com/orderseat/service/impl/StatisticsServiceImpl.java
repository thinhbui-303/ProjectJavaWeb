package com.orderseat.service.impl;

import com.orderseat.repository.ReservationRepository;
import com.orderseat.repository.UserRepository;
import com.orderseat.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public long getTotalReservations() {
        return reservationRepository.count();
    }

    @Override
    public double getTotalRevenue() {
        Double revenue = reservationRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public long getTotalCustomers() {
        // Đếm các user có quyền ROLE_CUSTOMER để thống kê số lượng khách hàng chính xác
        return userRepository.countCustomers(); 
    }

    @Override
    public Map<String, Long> getTopSellingProducts() {
        List<Object[]> results = reservationRepository.getTopSellingProducts();
        Map<String, Long> topProducts = new LinkedHashMap<>();
        
        // Lấy tối đa 5 sản phẩm đầu tiên
        results.stream().limit(5).forEach(result -> {
            topProducts.put((String) result[0], (Long) result[1]);
        });
        
        return topProducts;
    }
}
