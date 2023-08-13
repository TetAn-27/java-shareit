package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Item save(Item item);

    Item getById(int id);

    Page<Item> findAllByOwnerId(int userId, Pageable pageable);

    Page<Item> findByNameOrDescriptionContainingIgnoreCase(Pageable pageable, String nameSearch, String descriptionSearch);

    List<Item> findAllByRequestId(int requestId);
}
