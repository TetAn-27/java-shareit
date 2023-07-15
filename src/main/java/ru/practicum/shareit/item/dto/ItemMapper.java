package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(int userId, ItemDto itemDto) {
        return new Item(
                0,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                userId,
                null
        );
    }

    public static Item toItemForUpdate(int userId, ItemDto itemDto, Item item) {
        return new Item(
                item.getId(),
                itemDto.getName() != null ? itemDto.getName() :item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() :item.getDescription(),
                (Boolean)itemDto.isAvailable() != null ? itemDto.isAvailable() :item.isAvailable(),
                userId,
                null
        );
    }
}
