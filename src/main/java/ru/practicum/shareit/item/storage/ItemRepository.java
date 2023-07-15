package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    void createItem(Item item);
    Item updateItem(Item item);
    Item getItemById(int itemId);
}
