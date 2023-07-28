package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
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
        User user = new User();
        try {
            user = userRepository.getById(userId);

        } catch (DataIntegrityViolationException ex) {
            log.error("User с ID {} не был зарегестрирован", userId);
            throw new UserItemException("Такой пользователь не зарегестрирован");
        }
        return Optional.of(ItemMapper.toItemDto(itemRepository.save(
                ItemMapper.toItem(user, itemDto))));
    }

    @Override
    public Optional<ItemDto> update(int userId, Integer itemId, ItemDto itemDto) {
        Item item = getUpdateItem(itemId, ItemMapper.toItem(userRepository.getById(userId), itemDto));
        if (item.getOwner().getId() != userId) {
            throw new UserItemException("Вы не являетесь владельцем данной вещи");
        }
        return Optional.of(ItemMapper.toItemDto(itemRepository.save(item)));
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
    @Override
    public List<ItemDto> getAllUserItems(int userId) {
        return toListItemDto(itemRepository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return toListItemDto(itemRepository.findByNameOrDescriptionContainingIgnoreCase(
                text.toLowerCase(), text.toLowerCase()));
    }

    private List<ItemDto> toListItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    private Item getUpdateItem (Integer itemId, Item itemUpdate) {
        Item item = itemRepository.getById(itemId);
        item.setName(itemUpdate.getName() != null ? itemUpdate.getName() : item.getName());
        item.setDescription(itemUpdate.getDescription() != null ? itemUpdate.getDescription() : item.getDescription());
        item.setAvailable(itemUpdate.getAvailable() != null ? itemUpdate.getAvailable() : item.getAvailable());
        log.info("Предмет {} был обновлен", item.getName());
        return item;
    }
}
