package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Optional<ItemDto> create(int userId, ItemDto item) {
        itemRepository.createItem(ItemMapper.toItem(userId, item));
        return Optional.of(item);
    }

    @Override
    public Optional<ItemDto> update(int userId, Item item) {
        if (item.getId()!=userId) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        //Item itemUpdate = itemRepository.updateItem(Item item);
        return Optional.of(ItemMapper.toItemDto(itemRepository.updateItem(item).get()));
    }
}
