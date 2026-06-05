package com.orderseat.service;

import java.util.Map;

public interface StatisticsService {
    long getTotalReservations();
    double getTotalRevenue();
    long getTotalCustomers();
    Map<String, Long> getTopSellingProducts(); // Tên sản phẩm và số lượng bán ra
}
