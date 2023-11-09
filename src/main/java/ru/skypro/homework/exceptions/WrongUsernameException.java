package ru.skypro.homework.exceptions;

public class WrongUsernameException extends RuntimeException {
    public WrongUsernameException() {
        super();
    }

    public WrongUsernameException(String message) {
        super(message);
    }
}
