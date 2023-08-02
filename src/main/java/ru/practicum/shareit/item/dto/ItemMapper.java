package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoForGet toItemDtoForGet(Item item, BookingDtoRequest lastBooking, BookingDtoRequest nextBooking,
                                                List<Comment> comments) {
        return new ItemDtoForGet(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                nextBooking,
                lastBooking,
                comments

        );
    }

    public static Item toItem(User user, ItemDto itemDto) {
        return new Item(
                itemDto.getId() != null ? itemDto.getId() : 0,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                null
        );
    }
}
