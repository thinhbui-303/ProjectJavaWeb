package com.orderseat.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private MultipartFile imageFile;
}
