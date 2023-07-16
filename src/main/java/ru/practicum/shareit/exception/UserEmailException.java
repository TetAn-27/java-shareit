package ru.practicum.shareit.exception;

import javax.validation.ValidationException;

public class UserEmailException extends ValidationException {
    public UserEmailException(String message) {
        super(message);
    }
}
