package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static BookingDtoResponse toBookingDto(Booking booking) {
        return new BookingDtoResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDtoResponse.ItemDtoResponse(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDtoResponse.BookerDtoResponse(booking.getBooker().getId()),
                booking.getStatus()
        );
    }

    public static Booking toBooking(Item item, User user, BookingDtoRequest bookingDtoRequest) {
        return new Booking(
                0,
                bookingDtoRequest.getStart(),
                bookingDtoRequest.getEnd(),
                item,
                user,
                bookingDtoRequest.getStatus()
        );
    }
}
