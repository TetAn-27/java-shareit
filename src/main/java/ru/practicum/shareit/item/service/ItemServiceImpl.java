package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, UserService userService,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<ItemDto> create(int userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userId, userService.getUserById(userId).get());
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
    public Optional<ItemDtoForGet> getItemById(int userId, int itemId) {
        try {
            Item item = itemRepository.getById(itemId);
            BookingDtoRequest nextBooking =
                    item.getOwner().getId() == userId ? getItemNextBooking(itemId) : null;
            BookingDtoRequest lastBooking =
                    item.getOwner().getId() == userId ? getItemLastBooking(itemId) : null;
            return Optional.of(ItemMapper.toItemDtoForGet(item, lastBooking, nextBooking));
        } catch (EntityNotFoundException ex) {
            log.error("Предмет с ID {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
    }

    @Override
    public List<ItemDtoForGet> getAllUserItems(int userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        List<ItemDtoForGet> itemsDto = new ArrayList<>();
        for (Item item : items) {
            ItemDtoForGet itemDto = ItemMapper.toItemDtoForGet(item, getItemLastBooking(item.getId()),
                    getItemNextBooking(item.getId()));
            itemsDto.add(itemDto);
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return toListItemDto(itemRepository.findByNameOrDescriptionContainingIgnoreCase(
                text.toLowerCase(), text.toLowerCase())).stream()
                .filter(i -> i.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(int itemId) {
        try {
            return itemRepository.getById(itemId);
        } catch (EntityNotFoundException ex) {
            log.error("Предмет с ID {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким ID не был найден");
        }
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

    private BookingDtoRequest getItemLastBooking(Integer itemId) {
        try {
            return BookingMapper.toBookingDtoRequest(
                bookingRepository.findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
                        itemId, LocalDateTime.now(), Status.APPROVED));
        } catch (NullPointerException ex) {
            return null;
        }
    }

    private BookingDtoRequest getItemNextBooking(Integer itemId) {
        try {
            return BookingMapper.toBookingDtoRequest(
                    bookingRepository.findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
                            itemId, LocalDateTime.now(), Status.APPROVED));
        } catch (NullPointerException ex) {
            return null;
        }
    }
}
