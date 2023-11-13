package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MyMapper mapper;

    private final UserDetailsManager userDetailsManager;

    /**
     * method to update user password
     * @param passwordDto
     */
    public void updatePassword(PasswordDto passwordDto) {
        userDetailsManager.changePassword(passwordDto.getCurrentPassword(), passwordDto.getNewPassword());
    }

    /**
     * method to get info about registered user
     * @return
     */
    public UserDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(username);
        return mapper.map(userEntity);
    }

    public UserDto updateUserInfo(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntityOld = userRepository.findByUsername(username);
        UserEntity userEntityNew = mapper.map(userDto);
        userEntityNew.setId(userEntityOld.getId());
        userRepository.save(userEntityNew);
        return mapper.map(userEntityNew);
    }

    public String updateUserImage(String username, MultipartFile file) {
        return "done";
    }
}
