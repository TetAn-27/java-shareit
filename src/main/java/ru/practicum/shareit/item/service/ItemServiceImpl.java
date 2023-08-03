package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BookingValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
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
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, UserService userService,
                           BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
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
            return Optional.of(ItemMapper.toItemDtoForGet(item, lastBooking, nextBooking, getListComment(itemId)));
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
            int itemId = item.getId();
            ItemDtoForGet itemDto = ItemMapper.toItemDtoForGet(item, getItemLastBooking(itemId),
                    getItemNextBooking(itemId), getListComment(itemId));
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

    @Override
    public Optional<CommentDto> addComment(Integer userId, Integer itemId, CommentDto commentDto) {
        Booking booking;
        try {
            booking = bookingRepository.findFirst1ByItemIdAndBookerId(itemId, userId);
        } catch (NullPointerException ex) {
            log.error("К сожелению вы не можете оставить комментарий на эту вещь, так как вы не брали ее в аренду");
            throw new NotFoundException("Ошибка владения вещи");
        }
        Comment comment = CommentMapper.toComment(commentDto, itemRepository.getById(itemId),
                UserMapper.toUser(userId, userService.getUserById(userId).get()));
        if (booking.getEnd().isAfter(commentDto.getCreated())) {
            log.error("Ваше пользование вещью еще не закончилось. Вы можете оставить комментарий после " +
                    "того как закончится время бронирования");
            throw new BookingValidException("Ошибка временного пользования");
        }
        return Optional.of(CommentMapper.toCommentDto(commentRepository.save(comment)));
    }

    private List<ItemDto> toListItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    private List<CommentDto> getListComment(Integer itemId) {
        try {
            List<CommentDto> commentDtoList = new ArrayList<>();
            for (Comment comment : commentRepository.findAllByItemId(itemId)) {
                commentDtoList.add(CommentMapper.toCommentDto(comment));
            }
            return commentDtoList;
        } catch (NullPointerException ex) {
            return null;
        }
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
