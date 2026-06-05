package com.orderseat.entity.enums;

public enum ReservationStatus {
    PENDING,    // Đang chờ Admin duyệt
    CONFIRMED,  // Đã xác nhận / Khách chuẩn bị tới
    COMPLETED,  // Khách đã đến và Thanh toán xong
    CANCELLED   // Đã bị hủy (bởi Customer hoặc Admin)
}
