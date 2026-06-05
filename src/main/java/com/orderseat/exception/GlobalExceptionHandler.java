package com.orderseat.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", "Đã có lỗi hệ thống xảy ra: " + e.getMessage());
        return "error/generic_error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        model.addAttribute("errorMessage", "Lỗi nghiệp vụ: " + e.getMessage());
        return "error/generic_error";
    }
}
