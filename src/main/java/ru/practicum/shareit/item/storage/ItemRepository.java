package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Item save(Item item);

    Item getById(int Id);

    //List<Item> getAllUserItems(int userId);

    //List<Item> searchForItems(String text);
}
