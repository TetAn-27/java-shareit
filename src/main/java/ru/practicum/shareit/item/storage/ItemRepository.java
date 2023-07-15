package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public interface ItemRepository {
    void createItem(Item item);
    Optional<Item> updateItem(Item item);
}
