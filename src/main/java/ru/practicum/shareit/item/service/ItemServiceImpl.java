package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Optional<ItemDto> create(int userId, ItemDto itemDto) {
        itemRepository.createItem(ItemMapper.toItem(userId, itemDto));
        return Optional.of(itemDto);
    }

    @Override
    public Optional<ItemDto> update(int userId, Integer itemId, ItemDto itemDto) {
        if (itemRepository.getItemById(itemId).getOwner()!=userId) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(ItemMapper.toItemDto(itemRepository.updateItem(ItemMapper.toItem(userId, itemDto))));
    }

    @Override
    public Optional<ItemDto> getItemById(int itemId) {
        try {
            return Optional.of(ItemMapper.toItemDto(itemRepository.getItemById(itemId)));
        } catch (NotFoundException ex) {
            log.error("Предмет с ID {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }
}
