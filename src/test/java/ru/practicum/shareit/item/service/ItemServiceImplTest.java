package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingValidException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {


    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createItem_whenParametersValid_thenSavedItem() {
        Integer userId = 1;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        User user = new User();
        user.setId(userId);
        ItemDto itemDtoToSave = new ItemDto(1, "name", "description", true, 1);
        Item itemToSave = ItemMapper.toItem(user, itemRequest, itemDtoToSave);
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemRequestRepository.getById(anyInt())).thenReturn(itemRequest);
        when(itemRepository.save(itemToSave)).thenReturn(itemToSave);

        ItemDto actualItemDto = itemService.create(userId, itemDtoToSave).get();

        assertEquals(itemDtoToSave, actualItemDto);
        verify(itemRepository, times(1)).save(itemToSave);
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        int id = 1;
        User user = new User();
        user.setId(id);
        ItemRequest itemRequest = new ItemRequest();
        Item expectedItem = new Item(id, "name", "description", true, user, itemRequest);
        List<Comment> comments = new ArrayList<>();
        List<CommentDto> commentDto =
                ReflectionTestUtils.invokeMethod(itemService, "getListComment", anyInt());
        Booking nextBooking = new Booking();
        nextBooking.setId(1);
        nextBooking.setBooker(user);
        nextBooking.setItem(expectedItem);
        BookingDtoRequest nextBookingDto = BookingMapper.toBookingDtoRequest(nextBooking);
        Booking lastBooking = new Booking();
        lastBooking.setId(2);
        lastBooking.setBooker(user);
        lastBooking.setItem(expectedItem);
        BookingDtoRequest lastBookingDto = BookingMapper.toBookingDtoRequest(lastBooking);
        when(bookingRepository.findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
                id, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED)).thenReturn(lastBooking);
        when(bookingRepository.findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
                id, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED)).thenReturn(nextBooking);
        when(commentRepository.findAllByItemId(id)).thenReturn(comments);
        when(itemRepository.getById(anyInt())).thenReturn(expectedItem);

        ItemDtoForGet actualItemDto = itemService.getItemById(id,id).get();

        assertEquals(ItemMapper.toItemDtoForGet(expectedItem, lastBookingDto, nextBookingDto, commentDto), actualItemDto);
        verify(itemRepository, times(1)).getById(anyInt());
    }

    @Test
    void getItemById_whenParametersIsNull_thenReturnedItem() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item expectedItem = new Item(id, "name", "description", true, user, null);
        when(bookingRepository.findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
                id, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED)).thenReturn(null);
        when(bookingRepository.findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
                id, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED)).thenReturn(null);
        when(commentRepository.findAllByItemId(id)).thenReturn(null);
        when(itemRepository.getById(anyInt())).thenReturn(expectedItem);

        ItemDtoForGet actualItemDto = itemService.getItemById(id,id).get();

        assertEquals(ItemMapper.toItemDtoForGet(expectedItem, null, null, null), actualItemDto);
        verify(itemRepository, times(1)).getById(anyInt());
    }

    @Test
    void getItemById_whenItemNotFound_thenNotReturnedItem() {
        when(itemRepository.getById(anyInt())).thenThrow(EntityNotFoundException.class);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(1, 1));
    }

    @Test
    void getAllUserItems_whenRightConditions_thenReturnedList() {
        int id = 1;
        List<Item> expectedItems = new ArrayList<>();
        Page<Item> pageExpectedItems = new PageImpl<>(expectedItems);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(itemRepository.findAllByOwnerId(id, pageRequest)).thenReturn(pageExpectedItems);

        List<ItemDtoForGet> actualItemDto = itemService.getAllUserItems(id, pageRequest);
        List<ItemDtoForGet> expectedItemDto =
                ReflectionTestUtils.invokeMethod(itemService, "toListItemDtoForGet", expectedItems);

        assertEquals(expectedItemDto, actualItemDto);
        verify(itemRepository, times(1)).findAllByOwnerId(id, pageRequest);
    }

    @Test
    void searchForItems_whenRightConditions_thenReturnedList() {
        int id = 1;
        List<Item> expectedItems = new ArrayList<>();
        Page<Item> pageExpectedItems = new PageImpl<>(expectedItems);
        PageRequest pageRequest = PageRequest.of(0, 10);
        String text = "text";
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCase(pageRequest, text, text))
                .thenReturn(pageExpectedItems);

        List<ItemDto> actualItemDto = itemService.searchForItems(text, pageRequest);
        List<ItemDto> expectedItemDto =
                ReflectionTestUtils.invokeMethod(itemService, "toListItemDto", expectedItems);

        assertEquals(expectedItemDto, actualItemDto);
        verify(itemRepository, times(1)).
                findByNameOrDescriptionContainingIgnoreCase(pageRequest, text, text);
    }

    @Test
    void getById_whenItemFound_thenReturnedItem() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item expectedItem = new Item(id, "name", "description", true, user, null);
        when(itemRepository.getById(anyInt())).thenReturn(expectedItem);

        Item actualItem = itemService.getById(id);

        assertEquals(expectedItem, actualItem);
        verify(itemRepository, times(1)).getById(anyInt());
    }

    @Test
    void findAllByRequest_whenItemFound_thenReturnedItemList() {
        List<Item> expectedItems = new ArrayList<>();
        when(itemRepository.findAllByRequestId(anyInt())).thenReturn(expectedItems);

        List<ItemDto> actualItemDto = itemService.findAllByRequest(1);
        List<ItemDto> expectedItemDto =
                ReflectionTestUtils.invokeMethod(itemService, "toListItemDto", expectedItems);

        assertEquals(expectedItemDto, actualItemDto);
        verify(itemRepository, times(1)).findAllByRequestId(anyInt());
    }

    @Test
    void findAllByRequest_whenItemNotFound_thenReturnedNull() {
        when(itemRepository.findAllByRequestId(anyInt())).thenThrow(NullPointerException.class);

        List<ItemDto> actualItemDto = itemService.findAllByRequest(1);

        assertEquals(null, actualItemDto);
        verify(itemRepository, times(1)).findAllByRequestId(anyInt());
    }

    @Test
    void addComment_whenBookingEndIsAfterCommentCreated_thenBookingValidException() {
        int id = 1;
        User user = new User(
                id,
                "name",
                "name@eamil.com");
        ItemRequest itemRequest = new ItemRequest(
                id,
                "description",
                user,
                LocalDateTime.now()
        );
        Item item = new Item(
                id,
                "name",
                "description",
                true,
                user,
                itemRequest
        );
        Booking booking = new Booking(
                id,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(120),
                item,
                user,
                Status.WAITING);

        when(bookingRepository.findFirst1ByItemIdAndBookerId(id, id)).thenReturn(booking);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        CommentDto commentDtoToSave = new CommentDto(
                id,
                "text",
                id,
                id,
                null,
                LocalDateTime.now()
        );
        Comment commentToSave = CommentMapper.toComment(commentDtoToSave, item, user);

        assertThrows(BookingValidException.class, () -> itemService.addComment(id, id, commentDtoToSave));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void addComment_whenUserNotRentItem_thenNotFoundException() {
        int id = 1;
        User user = new User(
                id,
                "name",
                "name@eamil.com");
        User owner = new User(
                id+1,
                "owner",
                "owner@eamil.com");
        ItemRequest itemRequest = new ItemRequest(
                id,
                "description",
                user,
                LocalDateTime.now()
        );
        Item item = new Item(
                id,
                "name",
                "description",
                true,
                owner,
                itemRequest
        );
        Booking booking = new Booking(
                id,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(120),
                item,
                user,
                Status.WAITING);
        Comment commentToSave = new Comment(
                id,
                "text",
                item,
                user,
                LocalDateTime.now().plusMinutes(130));
        CommentDto commentDtoToSave = CommentMapper.toCommentDto(commentToSave);

        when(bookingRepository.findFirst1ByItemIdAndBookerId(id, id)).thenThrow(NullPointerException.class);

        assertThrows(NotFoundException.class, () -> itemService.addComment(id, id, commentDtoToSave));
        verify(commentRepository, never()).save(any(Comment.class));
    }
}