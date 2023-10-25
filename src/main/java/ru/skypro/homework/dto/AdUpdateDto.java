package ru.skypro.homework.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Contains information about updated ad
 */
@Data
public class AdUpdateDto {
    private String title;
    private String description;
    private BigDecimal price;
}
