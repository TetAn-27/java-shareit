package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDtoForGet toItemRequestDtoForGet(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestDtoForGet(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(User user, ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId() != null ? itemRequestDto.getId() : 0,
                itemRequestDto.getDescription(),
                user,
                itemRequestDto.getCreated()
        );
    }
}
