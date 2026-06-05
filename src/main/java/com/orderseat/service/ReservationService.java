package com.orderseat.service;

import com.orderseat.dto.CartItem;
import com.orderseat.entity.CoffeeTable;
import com.orderseat.entity.Reservation;

import java.util.List;
import java.util.Map;

public interface ReservationService {
    List<CoffeeTable> getAvailableTables();
    void createReservation(Reservation reservation, String username, Map<Long, CartItem> cart);
    void cancelReservation(Long reservationId, String username);
    List<Reservation> getHistoryByUsername(String username);
    List<Reservation> getAllReservations();
    void updateStatus(Long reservationId, com.orderseat.entity.enums.ReservationStatus status);
}
