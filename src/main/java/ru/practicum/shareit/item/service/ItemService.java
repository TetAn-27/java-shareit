package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(int userId, ItemDto itemDto);
    Optional<ItemDto>  update(int userId, Integer itemId, ItemDto itemDto);
    Optional<ItemDto> getItemById(int itemId);
    List<ItemDto> getAllUserItems(int userId);
    List<ItemDto> searchForItems(String text);
}
