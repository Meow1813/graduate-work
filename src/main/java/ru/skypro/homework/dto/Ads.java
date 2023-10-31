package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;

@Data
public class Ads {
    @Schema(description = "общее количество объявлений")
    private Integer count;
    private Collection<AdDto> results;
}
