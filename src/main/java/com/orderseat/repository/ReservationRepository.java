package com.orderseat.repository;

import com.orderseat.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserIdOrderByReservationTimeDesc(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(r.totalAmount) FROM Reservation r")
    Double getTotalRevenue();

    @org.springframework.data.jpa.repository.Query("SELECT rd.product.name, SUM(rd.quantity) FROM ReservationDetail rd GROUP BY rd.product.name ORDER BY SUM(rd.quantity) DESC")
    List<Object[]> getTopSellingProducts();

    @org.springframework.data.jpa.repository.Query("SELECT r FROM Reservation r WHERE r.coffeeTable.id = :tableId AND r.status = :status AND r.reservationTime BETWEEN :startTime AND :endTime")
    List<Reservation> findConflictingReservations(@org.springframework.data.repository.query.Param("tableId") Long tableId,
                                                  @org.springframework.data.repository.query.Param("status") com.orderseat.entity.enums.ReservationStatus status,
                                                  @org.springframework.data.repository.query.Param("startTime") java.time.LocalDateTime startTime,
                                                  @org.springframework.data.repository.query.Param("endTime") java.time.LocalDateTime endTime);
}
