package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(int userId, ItemDto item);
    Optional<ItemDto>  update(int userId, Item item);
}
