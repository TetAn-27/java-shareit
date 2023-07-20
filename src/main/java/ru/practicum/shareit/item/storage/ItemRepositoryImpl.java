package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int id = 0;

    @Override
    public Item createItem(Item item) {
        ++id;
        item.setId(id);
        items.put(id, item);
        log.info("Предмет {} был добавлен", item.getName());
        return item;
    }

    @Override
    public Item updateItem(Integer itemId, Item item, Item itemUpdate) {
        if (items.containsKey(itemId)) {
            item.setName(itemUpdate.getName() != null ? itemUpdate.getName() : item.getName());
            item.setDescription(itemUpdate.getDescription() != null ? itemUpdate.getDescription() : item.getDescription());
            item.setAvailable(itemUpdate.getAvailable() != null ? itemUpdate.getAvailable() : item.getAvailable());
            log.info("Предмет {} был обновлен", item.getName());
            return item;
        } else {
            throw new NotFoundException("Item с таким ID не был найден");
        }
    }

    @Override
    public Item getItemById(int itemId) {
        if (items.containsKey(itemId)) {
            log.debug("Предмет с id: {}", itemId);
            return items.get(itemId);
        } else {
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }

    @Override
    public List<Item> getAllUserItems(int userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == userId) {
                userItems.add(item);
            }
        }
        return userItems;
    }

    @Override
    public List<Item> searchForItems(String text) {
        return items.values().stream()
                .filter(i -> i.getAvailable()
                        && i.getDescription().toLowerCase().contains(text)
                        || i.getName().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
