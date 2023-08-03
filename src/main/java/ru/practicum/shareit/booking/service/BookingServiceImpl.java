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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StateException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;

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
        Item item = itemService.getById(itemId);
        itemService.getItemById(userId, itemId);
        if (!item.getAvailable()) {
            throw new BookingValidException("Вещь с таким ID не может быть сдана в аренду");
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Вы не можете взять в аренду свою вещь");
        }
        bookingDtoRequest.setStatus(Status.WAITING);
        return Optional.of(toBookingDto(bookingRepository.save(BookingMapper.toBooking
                (item, user, bookingDtoRequest))));
    }

    @Override
    public Optional<BookingDtoResponse> responseToRequest(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = getUpdateBooking(bookingId, approved);
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(toBookingDto(bookingRepository.save(booking)));
    }

    @Override
    public Optional<BookingDtoResponse> getBookingById(Integer userId, Integer bookingId) {
        try {
            Booking booking = bookingRepository.getById(bookingId);
        boolean isOwner = !Objects.equals(booking.getItem().getOwner().getId(), userId);
        boolean isBooker = !Objects.equals(booking.getBooker().getId(), userId);
        if (isOwner && isBooker) {
            throw new UserItemException("Вы не имеете доступа к просмортру информации о данной вещи");
        }

            return Optional.of(toBookingDto(booking));
        }
        catch (EntityNotFoundException ex) {
            log.error("Предмет с ID {} не был найден", bookingId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }

    @Override
    public List<BookingDtoResponse> getAllBookingByBookerId(Integer bookerId, String state) {
        userService.getUserById(bookerId);
        return toListBookingDto(getListAccordingState(bookingRepository.findAllByBookerId(bookerId), state));
    }

    @Override
    public List<BookingDtoResponse> getAllBookingByOwnerId(Integer ownerId, String state) {
        userService.getUserById(ownerId);
        return toListBookingDto(getListAccordingState(bookingRepository.findAllByItemOwnerId(ownerId), state));
    }

    private List<BookingDtoResponse> toListBookingDto(List<Booking> bookingList) {
        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoResponseList.add(toBookingDto(booking));
        }
        return bookingDtoResponseList;
    }

    private List<Booking> getListAccordingState(List<Booking> bookingList, String state) {
        switch (state) {
            case "ALL":
                return bookingList.stream()
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingList.stream()
                        .filter(i -> !i.getStatus().equals(Status.CANCELED)
                                && (i.getStart().equals(LocalDateTime.now())
                                || i.getStart().isBefore(LocalDateTime.now()))
                                && (i.getEnd().equals(LocalDateTime.now())
                                || i.getEnd().isAfter(LocalDateTime.now())))
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            case "PAST":
                return bookingList.stream()
                        .filter(i -> i.getStatus().equals(Status.APPROVED)
                                && i.getStart().isBefore(LocalDateTime.now())
                                && i.getEnd().isBefore(LocalDateTime.now()))
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingList.stream()
                        .filter(i -> i.getStatus().equals(Status.APPROVED)
                                || i.getStatus().equals(Status.WAITING)
                                && i.getStart().isAfter(LocalDateTime.now())
                                && i.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingList.stream()
                        .filter(i -> i.getStatus().equals(Status.WAITING))
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingList.stream()
                        .filter(i -> i.getStatus().equals(Status.REJECTED))
                        .sorted((o1, o2)->o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
            default:
                throw new StateException("Был указан неверный параметр фильтрации");
        }
    }

    private Booking getUpdateBooking (Integer bookingId, Boolean approved) {
        Booking booking = bookingRepository.getById(bookingId);
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BookingValidException("Вы не можете поменять статус бронирования");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return booking;
    }

    private void dateTimeValid(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new BookingValidException("Время окончания бронирования не может быть равно(или быть раньше)дате(-ы) начала");
        }
    }
}
