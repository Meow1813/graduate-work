package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Ad {
    @Schema(description = "id автора объявления")
    Integer author;
    @Schema(description = "ссылка на картинку объявления")
    String image;
    @Schema(description = "id объявления")
    Integer pk;
    @Schema(description = "цена объявления")
    Integer price;
    @Schema(description = "заголовок объявления")
    String title;
}
