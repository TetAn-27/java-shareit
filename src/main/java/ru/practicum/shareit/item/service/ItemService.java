package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

    ItemDto create(int userId, ItemDto item);
}
