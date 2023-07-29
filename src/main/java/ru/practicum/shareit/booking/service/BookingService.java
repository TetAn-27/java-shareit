package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<BookingDto> createRequest(BookingDto bookingDto);

    Optional<BookingDto> resposeToRequest(Integer userId, Integer bookingId, Boolean approved,BookingDto bookingDto);

    Optional<BookingDto> getBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getAllBookingByBookerId(Integer userId, Status state);

    List<BookingDto> getAllBookingByOwnerId(Integer userId, Status state);
}
