package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.io.IOException;


@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name="Пользователи")
public class UsersController {

    private final UserService userService;


    /**
     * endpoint 1 - updating password for existent user
     * @param passwordDto
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "/set_password")
    public void updatePassword(@RequestBody PasswordDto passwordDto) {
        userService.updatePassword(passwordDto);
    }

    /**
     * endpoint 2 - getting user info
     *
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInfo() {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(user);
    }

    /**
     * endpoint 3 - updating user info
     *
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userDto));
    }

    /**
     * updating user avatar
     * @param image - user picture image
     * @return - UserDto with image url for front-end
     * */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadAvatar(@RequestParam MultipartFile image,
                                               @Parameter(hidden = true) Authentication auth) throws IOException {
        String username = auth.getName();
        UserDto user = userService.updateUserImage(username, image);

        return ResponseEntity.ok(user);
    }

    /**
     * returns user avatar to front-end
     * */
    @GetMapping(value = "/avatars/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public byte[] getImage(@PathVariable("id") String id) throws IOException {
        log.info("Here is id {}", id);
        return userService.getAvatar(id);
    }
}
