package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<Item> create(int userId, ItemDto itemDto);
    Optional<Item>  update(int userId, Integer itemId, ItemDto itemDto);
    Optional<Item> getItemById(int itemId);
    List<Item> getAllUserItems(int userId);
    List<Item> searchForItems(String text);
}
