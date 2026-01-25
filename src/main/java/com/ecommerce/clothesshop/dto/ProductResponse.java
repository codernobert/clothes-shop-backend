package com.ecommerce.clothesshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String subCategory;
    private String brand;
    private String size;
    private String color;
    private String gender;
    private Integer stockQuantity;
    private String imageUrl;
    private Boolean isActive;
}
