package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<BookingDto> createRequest(BookingDto bookingDto) {
        return Optional.empty();
    }

    @Override
    public Optional<BookingDto> resposeToRequest(Integer userId, Integer bookingId, Boolean approved, BookingDto bookingDto) {
        return Optional.empty();
    }

    @Override
    public Optional<BookingDto> getBookingById(Integer userId, Integer bookingId) {
        return Optional.empty();
    }

    @Override
    public List<BookingDto> getAllBookingByBookerId(Integer userId, Status state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingByOwnerId(Integer userId, Status state) {
        return null;
    }
}
