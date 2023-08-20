package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, UserService userService,
                           BookingRepository bookingRepository, CommentRepository commentRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public Optional<ItemDto> create(int userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userId, userService.getUserById(userId).get());
        ItemRequest itemRequest = getItemRequest(itemDto.getRequestId());
        return Optional.of(ItemMapper.toItemDto(itemRepository.save(
                ItemMapper.toItem(user, itemRequest, itemDto))));
    }

    @Override
    public Optional<ItemDto> update(int userId, Integer itemId, ItemDto itemDto) {
        ItemRequest itemRequest = getItemRequest(itemDto.getRequestId());
        Item item = getUpdateItem(itemId, ItemMapper.toItem(userRepository.getById(userId), itemRequest, itemDto));
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
            log.error("Предмет с Id {} не был найден", itemId);
            throw new NotFoundException("Предмет с таким Id не был найден");
        }
    }

    @Override
    public List<ItemDtoForGet> getAllUserItems(int userId, PageRequest pageRequestMethod) {
        Pageable page = pageRequestMethod;
        do {
            Page<Item> pageRequest = itemRepository.findAllByOwnerId(userId, page);
            pageRequest.getContent().forEach(ItemRequest -> {
            });
            if(pageRequest.hasNext()){
                page = PageRequest.of(pageRequest.getNumber() + 1, pageRequest.getSize(), pageRequest.getSort());
                page = null;
            }
        return toListItemDtoForGet(pageRequest.getContent());
        } while (page != null);
    }

    @Override
    public List<ItemDto> searchForItems(String text, PageRequest pageRequestMethod) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        Pageable page = pageRequestMethod;
        do {
            Page<Item> pageRequest = itemRepository.findByNameOrDescriptionContainingIgnoreCase(page,
                    text.toLowerCase(), text.toLowerCase());
            pageRequest.getContent().forEach(ItemRequest -> {
            });
            if(pageRequest.hasNext()){
                page = PageRequest.of(pageRequest.getNumber() + 1, pageRequest.getSize(), pageRequest.getSort());
                page = null;
            }
            return toListItemDto(pageRequest.getContent()).stream()
                    .filter(ItemDto::getAvailable)
                    .collect(Collectors.toList());
        } while (page != null);
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
            log.error("Ошибка владения вещи");
            throw new NotFoundException("К сожелению, вы не можете оставить комментарий на эту вещь, " +
                    "так как вы не брали ее в аренду");
        }
        Comment comment = CommentMapper.toComment(commentDto, itemRepository.getById(itemId),
                UserMapper.toUser(userId, userService.getUserById(userId).get()));
        if (booking.getEnd().isAfter(commentDto.getCreated())) {
            log.error("Ошибка временного пользования");
            throw new BookingValidException("Ваше пользование вещью еще не закончилось. Вы можете оставить комментарий " +
                    "после того как закончится время бронирования");
        }
        return Optional.of(CommentMapper.toCommentDto(commentRepository.save(comment)));
    }

    @Override
    public List<ItemDto> findAllByRequest(int requestId) {
        List<Item> items;
        try {
            items = itemRepository.findAllByRequestId(requestId);
        } catch (NullPointerException ex) {
            log.info("На данный запрос ответы отсутсвуют");
            return null;
        }
        return toListItemDto(items);
    }

    private List<ItemDto> toListItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    private List<ItemDtoForGet> toListItemDtoForGet(List<Item> itemList) {
        List<ItemDtoForGet> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            int itemId = item.getId();
            itemDtoList.add(ItemMapper.toItemDtoForGet(item, getItemLastBooking(itemId),
                    getItemNextBooking(itemId), getListComment(itemId)));
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
            log.info("Комментарии к данной вещи отсутствуют");
            return null;
        }
    }

    private Item getUpdateItem(Integer itemId, Item itemUpdate) {
        Item item = itemRepository.getById(itemId);
        item.setName(itemUpdate.getName() != null ? itemUpdate.getName() : item.getName());
        item.setDescription(itemUpdate.getDescription() != null ? itemUpdate.getDescription() : item.getDescription());
        item.setAvailable(itemUpdate.getAvailable() != null ? itemUpdate.getAvailable() : item.getAvailable());
        log.debug("Предмет {} был обновлен", item.getName());
        return item;
    }

    private BookingDtoRequest getItemLastBooking(Integer itemId) {
        try {
            return BookingMapper.toBookingDtoRequest(
                bookingRepository.findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
                        itemId, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED));
        } catch (NullPointerException ex) {
            log.error("Вещь не забронирована");
            return null;
        }
    }

    private BookingDtoRequest getItemNextBooking(Integer itemId) {
        try {
            return BookingMapper.toBookingDtoRequest(
                    bookingRepository.findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
                            itemId, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED));
        } catch (NullPointerException ex) {
            log.error("Вещь не забронирована");
            return null;
        }
    }

    private ItemRequest getItemRequest(Integer requestId) {
        if (requestId == null) {
            return null;
        }
        try {
            return itemRequestRepository.getById(requestId);
        } catch (IllegalArgumentException ex) {
            log.error("На вещь пока нет запросов");
            return null;
        }
    }
}
