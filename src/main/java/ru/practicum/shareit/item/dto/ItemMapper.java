package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

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
