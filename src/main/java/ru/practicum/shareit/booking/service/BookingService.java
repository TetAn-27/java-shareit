package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<BookingDtoResponse> createRequest(Integer userId, BookingDtoRequest bookingDtoRequest);

    Optional<BookingDtoResponse> responseToRequest(Integer userId, Integer bookingId, Boolean approved, BookingDtoRequest bookingDtoRequest);

    Optional<BookingDtoResponse> getBookingById(Integer userId, Integer bookingId);

    List<BookingDtoResponse> getAllBookingByBookerId(Integer userId, Status state);

    //List<BookingDto> getAllBookingByOwnerId(Integer userId, Status state);
}
