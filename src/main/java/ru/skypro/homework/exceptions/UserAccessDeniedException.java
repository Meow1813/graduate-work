package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAccessDeniedException extends RuntimeException{
    public UserAccessDeniedException() {
        super();
    }
    public UserAccessDeniedException(String message) {
        super(message);
    }
}
