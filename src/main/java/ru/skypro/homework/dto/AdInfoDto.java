package ru.skypro.homework.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Contains detailed information about ad
 */
@Data
public class AdInfoDto {
    private int pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private BigDecimal price;
    private String title;
}
