package ru.practicum.shareit.exception;

public class UserItemException extends NotFoundException {
    public UserItemException(String message) {
        super(message);
    }
}
