package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.UserItemException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<BookingDto> createRequest(BookingDto bookingDto) {
        bookingDto.setStatus(Status.WAITING);
        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto))));
    }

    @Override
    public Optional<BookingDto> resposeToRequest(Integer userId, Integer bookingId, Boolean approved, BookingDto bookingDto) {
        Booking booking = getUpdateBooking(bookingId, approved, BookingMapper.toBooking(bookingDto));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(booking)));
    }

    @Override
    public Optional<BookingDto> getBookingById(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        if (!Objects.equals(booking.getBooker().getId(), userId)) {
            throw new UserItemException("Вы не являетесь автором бронирования данной вещи");
        }
        return Optional.of(BookingMapper.toBookingDto(booking));
    }

    @Override
    public List<BookingDto> getAllBookingByBookerId(Integer bookerId, Status state) {
        return toListBookingDto(bookingRepository.findAllByBookerId(bookerId));
    }

    /*@Override
    public List<BookingDto> getAllBookingByOwnerId(Integer ownerId, Status state) {
        return toListBookingDto(bookingRepository.findAllByOwnerId(ownerId));
    }*/

    private List<BookingDto> toListBookingDto(List<Booking> bookingList) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoList.add(BookingMapper.toBookingDto(booking));
        }
        return bookingDtoList;
    }

    private Booking getUpdateBooking (Integer bookingId, Boolean approved, Booking bookingUpdate) {
        Booking booking = bookingRepository.getById(bookingId);
        booking.setStart(bookingUpdate.getStart() != null ? bookingUpdate.getStart() : booking.getStart());
        booking.setEnd(bookingUpdate.getEnd() != null ? bookingUpdate.getEnd()  : booking.getEnd() );
        booking.setItem(bookingUpdate.getItem() != null ? bookingUpdate.getItem() : booking.getItem());
        booking.setBooker(bookingUpdate.getBooker() != null ? bookingUpdate.getBooker() : booking.getBooker());
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return booking;
    }
}
