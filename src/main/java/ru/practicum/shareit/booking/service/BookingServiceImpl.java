package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingValidException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public Optional<BookingDtoResponse> createRequest(Integer userId, BookingDtoRequest bookingDtoRequest) {
        dateTimeValid(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd());
        int itemId = bookingDtoRequest.getItemId();
        User user = UserMapper.toUser(userId, userService.getUserById(userId).get());
        Item item = ItemMapper.toItem(user, itemService.getItemById(itemId).get());
        if (!item.getAvailable()) {
            throw new BookingValidException("Вещь с таким ID не может быть сдана в аренду");
        }
        bookingDtoRequest.setStatus(Status.WAITING);
        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking
                    (item, user, bookingDtoRequest))));
    }

    @Override
    public Optional<BookingDtoResponse> responseToRequest(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = getUpdateBooking(bookingId, approved);
        //User user = UserMapper.toUser(userId, userService.getUserById(userId).get());
        //Item item = ItemMapper.toItem(user, itemService.getItemById(booking.getItem().getId()).get());
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(booking)));
    }

    @Override
    public Optional<BookingDtoResponse> getBookingById(Integer userId, Integer bookingId) {
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
    public List<BookingDtoResponse> getAllBookingByBookerId(Integer bookerId, Status state) {
        return toListBookingDto(bookingRepository.findAllByBookerId(bookerId));
    }

    /*@Override
    public List<BookingDto> getAllBookingByOwnerId(Integer ownerId, Status state) {
        return toListBookingDto(bookingRepository.findAllByOwnerId(ownerId));
    }*/

    private List<BookingDtoResponse> toListBookingDto(List<Booking> bookingList) {
        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoResponseList.add(BookingMapper.toBookingDto(booking));
        }
        return bookingDtoResponseList;
    }

    /*private Booking getUpdateBooking (Integer bookingId, Boolean approved, Booking bookingUpdate) {
        Booking booking = bookingRepository.getById(bookingId);
        booking.setStart(bookingUpdate.getStart() != null ? bookingUpdate.getStart() : booking.getStart());
        booking.setEnd(bookingUpdate.getEnd() != null ? bookingUpdate.getEnd()  : booking.getEnd() );
        booking.setItem(bookingUpdate.getItem() != null ? bookingUpdate.getItem() : booking.getItem());
        booking.setBooker(bookingUpdate.getBooker() != null ? bookingUpdate.getBooker() : booking.getBooker());
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return booking;
    }*/

    private Booking getUpdateBooking (Integer bookingId, Boolean approved) {
        Booking booking = bookingRepository.getById(bookingId);
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return booking;
    }

    private void dateTimeValid(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new BookingValidException("Время окончания бронирования не может быть равно(или быть раньше)дате(-ы) начала");
        }
    }
}
