package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateOrUpdateAd {
    @Schema(description = "заголовок объявления")
    private String title;
    @Schema(description = "цена объявления")
    private Integer price;
    @Schema(description = "описание объявления")
    private String description;

}
