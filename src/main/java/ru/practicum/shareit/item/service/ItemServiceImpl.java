package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ItemDto> create(int userId, ItemDto itemDto) {
        if (userRepository.getById(userId) == null) {
            throw new UserItemException("Такой пользователь не зарегестрирован");
        }
        return Optional.of(ItemMapper.toItemDto(itemRepository.save(
                ItemMapper.toItem(userRepository.getById(userId), itemDto))));
    }

    @Override
    public Optional<ItemDto> update(int userId, Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.getById(itemId);
        if (item.getOwner().getId() != userId) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(ItemMapper.toItemDto(itemRepository.save(
                ItemMapper.toItem(userRepository.getById(userId), itemDto))));
    }

    @Override
    public Optional<ItemDto> getItemById(int itemId) {
        try {
            return Optional.of(ItemMapper.toItemDto(itemRepository.getById(itemId)));
        } catch (EntityNotFoundException ex) {
            log.error("Предмет с ID {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }

   /* @Override
    public List<ItemDto> getAllUserItems(int userId) {
        return toListItemDto(itemRepository.getAllUserItems(userId));
    }*/

    /*@Override
    public List<ItemDto> searchForItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return toListItemDto(itemRepository.searchForItems(text.toLowerCase()));
    }*/

    private List<ItemDto> toListItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
