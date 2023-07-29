package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

public class BookingMapper {

    private final ItemService itemService;
    private final UserService userService;

    public BookingMapper(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem() != null ? booking.getItem().getId() : null,
                booking.getBooker() != null ? booking.getBooker().getId() : null,
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                0,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null, //ItemMapper.toItem(itemService.getItemById(bookingDto.getItem()).get()),
                null, //UserMapper.toUser(userService.getUserById(bookingDto.getBooker()).get()),
                bookingDto.getStatus()
        );
    }
}
