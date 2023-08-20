package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingValidException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserItemException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createRequest_whenParametersValid_thenSavedUser() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        Item item = new Item(id, "name", "description", true, owner, null);
        item.setId(id);
        BookingDtoRequest bookingDtoToSave =
                new BookingDtoRequest(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), id, id, Status.WAITING);
        Booking bookingToSave = BookingMapper.toBooking(item, user, bookingDtoToSave);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemService.getById(id)).thenReturn(item);
        when(itemService.getItemById(id, id)).thenReturn(Optional.of(ItemMapper.toItemDtoForGet(item, null, null, null)));
        when(bookingRepository.save(bookingToSave)).thenReturn(bookingToSave);

        BookingDtoResponse actualBookingDto = bookingService.createRequest(id, bookingDtoToSave).get();
        BookingDtoResponse expectedBookingDto = BookingMapper.toBookingDto(bookingToSave);

        assertEquals(expectedBookingDto, actualBookingDto);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createRequest_whenDtaTimeNotValid_thenSavedUser() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        Item item = new Item(id, "name", "description", true, owner, null);
        item.setId(id);
        BookingDtoRequest bookingDtoToSave =
                new BookingDtoRequest(id, LocalDateTime.now(), LocalDateTime.now(), id, id, Status.WAITING);

        assertThrows(BookingValidException.class, () -> bookingService.createRequest(id, bookingDtoToSave));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createRequest_whenAvailableIsFalse_thenBookingValidException() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", false, user, null);
        item.setId(id);
        BookingDtoRequest bookingDtoToSave =
                new BookingDtoRequest(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), id, id, null);
        Booking bookingToSave = BookingMapper.toBooking(item, user, bookingDtoToSave);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemService.getById(id)).thenReturn(item);
        when(itemService.getItemById(id, id)).thenReturn(Optional.of(ItemMapper.toItemDtoForGet(item, null, null, null)));

        assertThrows(BookingValidException.class, () -> bookingService.createRequest(id, bookingDtoToSave));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createRequest_whenUserEqualsOwnerItem_thenNotFoundException() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        BookingDtoRequest bookingDtoToSave =
                new BookingDtoRequest(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), id, id, null);
        Booking bookingToSave = BookingMapper.toBooking(item, user, bookingDtoToSave);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemService.getById(id)).thenReturn(item);
        when(itemService.getItemById(id, id)).thenReturn(Optional.of(ItemMapper.toItemDtoForGet(item, null, null, null)));

        assertThrows(NotFoundException.class, () -> bookingService.createRequest(id, bookingDtoToSave));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void responseToRequest_whenParametersValid_thenUpdateUser() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        Booking expectedBooking =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item, user, Status.WAITING);
        when(bookingRepository.getById(id)).thenReturn(expectedBooking);
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        BookingDtoResponse actualBookingDto = bookingService.responseToRequest(id, id, true).get();
        BookingDtoResponse expectedBookingDto = BookingMapper.toBookingDto(expectedBooking);

        assertEquals(expectedBookingDto, actualBookingDto);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void responseToRequest_whenUserNotEqualsOwnerItem_thenUserItemException() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        Booking expectedBooking =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item, user, Status.WAITING);
        when(bookingRepository.getById(id)).thenReturn(expectedBooking);

        assertThrows(UserItemException.class, () -> bookingService.responseToRequest(2, id, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void responseToRequest_whenStatusNotWaiting_thenBookingValidException() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        Booking expectedBooking =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item, user, Status.APPROVED);
        when(bookingRepository.getById(id)).thenReturn(expectedBooking);

        assertThrows(BookingValidException.class, () -> bookingService.responseToRequest(1, id, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingById_whenBookingFound_thenReturnedBooking() {
        int id = 1;
        User user = new User();
        user.setId(id);
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        Booking expectedBooking =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item, user, Status.APPROVED);
        when(bookingRepository.getById(id)).thenReturn(expectedBooking);

        BookingDtoResponse actualBookingDto = bookingService.getBookingById(id, id).get();
        BookingDtoResponse expectedBookingDto = BookingMapper.toBookingDto(expectedBooking);

        assertEquals(expectedBookingDto, actualBookingDto);
        verify(bookingRepository, times(1)).getById(anyInt());
    }

    @Test
    void getBookingById_whenUserNotEqualsOwnerItemAndUserNotEqualsBooker_thenUserItemException() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item = new Item(id, "name", "description", true, user, null);
        item.setId(id);
        Booking expectedBooking =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item, user, Status.APPROVED);
        when(bookingRepository.getById(id)).thenReturn(expectedBooking);

        assertThrows(UserItemException.class, () -> bookingService.getBookingById(2, id));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenNotFoundException() {
        int id = 1;
        when(bookingRepository.getById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(id, id));
    }

    @Test
    void getAllBookingByBookerId_whenStateAll_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item1, user, Status.WAITING);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(100), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        expectedBookings.add(expectedBooking2);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "ALL", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByBookerId_whenStateWaiting_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item1, user, Status.WAITING);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(100), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "WAITING", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByBookerId_whenStateCurrent_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), item1, user, Status.WAITING);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now().plusMinutes(50), LocalDateTime.now().plusMinutes(100), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "CURRENT", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByBookerId_whenStatePast_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now().minusMinutes(50), LocalDateTime.now(), item1, user, Status.APPROVED);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(100), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "PAST", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByBookerId_whenStateFuture_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now().plusMinutes(50), LocalDateTime.now().plusMinutes(100), item1, user, Status.WAITING);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(50), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "FUTURE", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByBookerId_whenStateRejected_thenReturnedBookingList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        Item item1 = new Item(1, "name1", "description1", true, user, null);
        item1.setId(id);
        Booking expectedBooking1 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(50), item1, user, Status.REJECTED);
        Item item2 = new Item(2, "name2", "description2", true, user, null);
        item2.setId(id);
        Booking expectedBooking2 =
                new Booking(id, LocalDateTime.now(), LocalDateTime.now().plusMinutes(50), item2, user, Status.APPROVED);
        List<Booking> checkBookingList = new ArrayList<>();
        checkBookingList.add(expectedBooking1);
        checkBookingList.add(expectedBooking2);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(expectedBooking1);
        Page<Booking> pageExpectedBookings = new PageImpl<>(checkBookingList);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByBookerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByBookerId(id, "REJECTED", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByBookerId(id, pageRequest);
    }

    @Test
    void getAllBookingByOwnerId_whenRightConditions_thenReturnedList() {
        int id = 1;
        User user = new User(id, "name", "name@mail.ru");
        List<Booking> expectedBookings = new ArrayList<>();
        Page<Booking> pageExpectedBookings = new PageImpl<>(expectedBookings);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(userService.getUserById(id)).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(bookingRepository.findAllByItemOwnerId(id, pageRequest)).thenReturn(pageExpectedBookings);

        List<BookingDtoResponse> actualBookingDtoResponse = bookingService.getAllBookingByOwnerId(id, "WAITING", pageRequest);
        List<BookingDtoResponse> expectedBookingDtoResponse =
                ReflectionTestUtils.invokeMethod(bookingService, "toListBookingDto", expectedBookings);

        assertEquals(actualBookingDtoResponse, expectedBookingDtoResponse);
        verify(bookingRepository, times(1)).findAllByItemOwnerId(id, pageRequest);
    }
}