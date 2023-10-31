package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.utils.MyMapper;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final MyMapper mapper;

    public String uploadAvatar(String username, MultipartFile file) {
        return "done";
    }
}
