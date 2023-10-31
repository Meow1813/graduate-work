package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    @Schema(description = "id  комментария")
    private Integer pk;

    @Schema(description = "id  автора комментария")
    private Long author;

    @Schema(description = "ссылка на  аватор автора(пользователя) комментария")
    private String authorImage;

    @Schema(description = "имя автора комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария")
    private Long createdAt;

     @Schema(description = "Текст комментария")
    private String text;
}
