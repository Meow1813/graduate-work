package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MyMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MyMapper mapper;

    private final UserDetailsManager userDetailsManager;

    @Value("${avatar.image.dir}")
    private String avatarDir;

    /**
     * method to update user password
     * @param passwordDto containing old and new password
     */
    public void updatePassword(PasswordDto passwordDto) {
        userDetailsManager.changePassword(passwordDto.getCurrentPassword(), passwordDto.getNewPassword());
    }

    /**
     * method to get info about registered user
     * @return UserDto with information
     */
    public UserDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(username);
        return mapper.map(userEntity);
    }

    /**
     * updates user info
     * @param userDto with new info
     * @return UserDto with updated info
     * */
    public UserDto updateUserInfo(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(username);
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPhone(userDto.getPhone());
        userRepository.save(userEntity);
        return mapper.map(userEntity);
    }

    /**
     * uploads user avatar image
     * @param username - use name(email)
     * @param file - user image
     * @return UserDto with url to access user image by frontend
     * */
    public UserDto updateUserImage(String username, MultipartFile file) throws IOException {
        String fileName = username.replaceAll("[@.]", "_")
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path path = Path.of(avatarDir, fileName);
        UserEntity user = userRepository.findByUsername(username);

        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        Files.write(path, file.getBytes());
        user.setImage("/users/" + path);
        userRepository.save(user);

        return mapper.map(user);
    }

    /**
     * sends user avatar to frontend
     * @param id - picture id
     * @return - byte array with picture
     * */
    public byte[] getAvatar(String id) throws IOException {
        Path path = Path.of(avatarDir, id);
        return Files.readAllBytes(path);
    }
}
