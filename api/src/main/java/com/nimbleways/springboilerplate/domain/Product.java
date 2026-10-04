package com.nimbleways.springboilerplate.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private Integer leadTime;
    private Integer availableStock;
    private ProductType type;
    private String name;
    private LocalDate expiryDate;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
}
