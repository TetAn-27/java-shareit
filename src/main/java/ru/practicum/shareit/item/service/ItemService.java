package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    ItemDto create(int userId, ItemDto item);
    ItemDto update(int userId, Item item);
}
