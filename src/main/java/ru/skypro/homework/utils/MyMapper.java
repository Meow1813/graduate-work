package ru.skypro.homework.utils;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

@AllArgsConstructor
public class MyMapper {
    private final ModelMapper modelMapper;

    /*
    * Here methods returning Dto come
    * */

    /**
     * Converts UserEntity to UserDto taking into account that email is username
     * @param entity
     * @return dto
     */
    public UserDto map(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setImage(entity.getImage());

        return dto;
    }

    /**
     * Converts AdEntity to AdDto
     * @param entity
     * @return
     */
    public AdDto map(AdEntity entity) {
        return modelMapper.map(entity, AdDto.class);
    }

    /**
     * Converts AdEntity to AdInfoDto unpacking AuthorEntity.
     * Second parameter of type boolean used just to overload method and has no effect
     * @param entity
     * @param isInfo
     * @return
     */
    public AdInfoDto map(AdEntity entity, boolean isInfo) {
        AdInfoDto dto = new AdInfoDto();

        dto.setPk(entity.getPk());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getUsername());
        dto.setImage(entity.getImage());
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        return dto;
    }

    /**
     * Conveerts CommentEntity to CommentDto parsing LocalDateTime to Milliseconds and Author to necessary fields
     * @param entity
     * @return
     */
    private CommentDto map(CommentEntity entity){
        CommentDto dto = new CommentDto();

        dto.setPk(entity.getPk());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(entity.getAuthor().getImage());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        ZonedDateTime zdt = ZonedDateTime.of(entity.getCreatedAt(), ZoneId.systemDefault());
        dto.setCreatedAt(zdt.toInstant().toEpochMilli());
        dto.setText(entity.getText());

        return dto;
    }

    /*
    * Here methods returning Entity come
    * */

    private CommentEntity map(CommentDto comment){
        return modelMapper.map(comment, CommentEntity.class);
    }

    private AdEntity map(AdDto ad) {
        return modelMapper.map(ad, AdEntity.class);
    }

    public UserEntity map(RegisterUserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }
}
