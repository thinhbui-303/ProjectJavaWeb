package com.orderseat.entity;

import com.orderseat.entity.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coffee_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TableStatus status;
}
