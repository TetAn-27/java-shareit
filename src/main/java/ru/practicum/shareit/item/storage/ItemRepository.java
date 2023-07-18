package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item createItem(Item item);

    Item updateItem(Integer itemId, Item item);

    Item getItemById(int itemId);

    List<Item> getAllUserItems(int userId);

    List<Item> searchForItems(String text);
}
