package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoForGet {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private BookingDtoRequest nextBooking;
    private BookingDtoRequest lastBooking;
    private List<CommentDto> comments;
}
