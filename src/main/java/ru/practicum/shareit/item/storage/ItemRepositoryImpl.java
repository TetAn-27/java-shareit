package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int id = 0;
    @Override
    public void createItem(Item item) {
        ++id;
        item.setId(id);
        items.put(id, item);
        log.info("Предмет {} был добавлен", item.getName());
    }

    @Override
    public Optional<Item> updateItem(Item item) {
        if (items.containsKey(item.getId())) {
            items.put(item.getId(), item);
            log.info("Предмет {} был обновлен", item.getName());
            return Optional.of(item);
        } else {
            throw new NotFoundException("Item с таким ID не был найден");
        }
    }
}
