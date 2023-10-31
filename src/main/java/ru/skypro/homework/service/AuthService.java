package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterUserDto;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterUserDto register);
}
