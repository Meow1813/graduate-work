package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrUpdateComment {
    @Schema(description = "текст комментария",minLength = 8,maxLength = 64)
    private String text;
}
