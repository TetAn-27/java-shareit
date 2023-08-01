package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDtoForGet {
    private Integer id;
    @NotNull
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание фильма превышает 200 символов")
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private Integer request;
    private BookingDtoResponse nextBooking;
    private BookingDtoResponse lastBooking;
}
