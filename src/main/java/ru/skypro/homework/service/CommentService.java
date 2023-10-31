package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.utils.MyMapper;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {
    private final MyMapper mapper;
}
