package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AvatarService {
    private static Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public String uploadAvatar(String username, MultipartFile file) {
        return "done";
    }
}
