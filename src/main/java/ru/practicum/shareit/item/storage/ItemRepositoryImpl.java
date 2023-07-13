package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int id = 0;
    @Override
    public void createItem(Item item) {
        ++id;
        item.setId(id);
        items.put(id, item);
    }
}
