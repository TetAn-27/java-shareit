package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Optional<Item> create(int userId, ItemDto itemDto) {
        //itemRepository.createItem(ItemMapper.toItem(userId, itemDto));
        return Optional.of(itemRepository.createItem(ItemMapper.toItem(userId, itemDto)));
    }

    @Override
    public Optional<Item> update(int userId, Integer itemId, ItemDto itemDto) {
        if (itemRepository.getItemById(itemId).getOwner()!=userId) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(itemRepository.updateItem(itemId,
                ItemMapper.toItemForUpdate(userId, itemDto, getItemById(itemId).get())));
    }

    @Override
    public Optional<Item> getItemById(int itemId) {
        try {
            return Optional.of(itemRepository.getItemById(itemId));
        } catch (NotFoundException ex) {
            log.error("Предмет с ID {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }

    @Override
    public List<Item> getAllUserItems(int userId) {
        return itemRepository.getAllUserItems(userId);
    }

    @Override
    public List<Item> searchForItems(String text) {
        return itemRepository.searchForItems(text);
    }

    /*private List<ItemDto> toListItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }*/
}
