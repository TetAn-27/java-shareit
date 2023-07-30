package ru.practicum.shareit.exception;

public class BookingValidException extends RuntimeException {
    public BookingValidException(String message) {
        super(message);
    }
}
