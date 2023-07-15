package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    void createItem(Item item);
    Item updateItem(Item item);
    Item getItemById(int itemId);
    List<Item> getAllUserItems(int userId);
}
