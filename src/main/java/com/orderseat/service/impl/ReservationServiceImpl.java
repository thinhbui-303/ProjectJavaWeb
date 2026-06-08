package com.orderseat.service.impl;

import com.orderseat.dto.CartItem;
import com.orderseat.entity.*;
import com.orderseat.entity.enums.ReservationStatus;
import com.orderseat.entity.enums.TableStatus;
import com.orderseat.repository.*;
import com.orderseat.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CoffeeTableRepository coffeeTableRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CoffeeTable> getAvailableTables() {
        return coffeeTableRepository.findAll().stream()
                .filter(t -> t.getStatus() == TableStatus.AVAILABLE)
                .toList();
    }

    @Override
    @Transactional
    public void createReservation(Reservation reservation, String username, Map<Long, CartItem> cart) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.PENDING);
        if (reservation.getReservationTime() == null) {
            reservation.setReservationTime(LocalDateTime.now().plusHours(2));
        }

        // Kiểm tra bàn có đang rảnh không
        CoffeeTable table = coffeeTableRepository.findById(reservation.getCoffeeTable().getId())
                .orElseThrow(() -> new RuntimeException("Bàn không tồn tại"));
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn này hiện không trống. Vui lòng chọn bàn khác.");
        }

        // Lưu Reservation trước để có ID
        reservationRepository.save(reservation);

        // Tạo ReservationDetail từ từng item trong giỏ hàng
        double total = 0.0;
        if (cart != null && !cart.isEmpty()) {
            for (CartItem item : cart.values()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + item.getProductId()));

                ReservationDetail detail = ReservationDetail.builder()
                        .reservation(reservation)
                        .product(product)
                        .quantity(item.getQuantity())
                        .unitPrice(item.getPrice())
                        .build();
                reservation.getDetails().add(detail);
                total += item.getPrice() * item.getQuantity();
            }
        }

        reservation.setTotalAmount(total > 0 ? total : null);
        // Save lần 2 để cascade lưu ReservationDetail và cập nhật totalAmount
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, String username) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt bàn"));

        // Kiểm tra quyền: đơn phải thuộc về user hiện tại
        if (!reservation.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền hủy đơn này");
        }

        // Chỉ hủy được đơn đang ở trạng thái Chờ Duyệt
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn đang ở trạng thái Chờ Duyệt");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getHistoryByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return reservationRepository.findByUserIdOrderByReservationTimeDesc(user.getId());
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getReservationTime().compareTo(r1.getReservationTime()))
                .toList();
    }

    @Override
    @Transactional
    public void updateStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt bàn"));
        reservation.setStatus(status);
        
        CoffeeTable table = reservation.getCoffeeTable();
        if (status == ReservationStatus.CONFIRMED) {
            table.setStatus(TableStatus.OCCUPIED);
        } else if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            table.setStatus(TableStatus.AVAILABLE);
        }
        
        reservationRepository.save(reservation);
    }
}
