package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForGet;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void createItemRequest_whenParametersValid_thenSavedItemRequest() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        ItemRequestDto itemRequestDtoToSaved = new ItemRequestDto(1, "name", LocalDateTime.now());
        ItemRequest itemRequestToSave = ItemRequestMapper.toItemRequest(user, itemRequestDtoToSaved);
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemRequestRepository.save(itemRequestToSave)).thenReturn(itemRequestToSave);

        ItemRequestDto actualItemRequestDto = itemRequestService.create(userId, itemRequestDtoToSaved).get();

        assertEquals(itemRequestDtoToSaved, actualItemRequestDto);
        verify(itemRequestRepository, times(1)).save(itemRequestToSave);
    }

    @Test
    void findAll_whenRightConditions_thenReturnedList() {
        Integer userId = 1;
        List<ItemRequest> expectedItemRequest = new ArrayList<>();
        when(itemRequestRepository.findAllByRequesterId(anyInt())).thenReturn(expectedItemRequest);

        List<ItemRequestDtoForGet> actualItemRequestDto = itemRequestService.findAll(userId);
        List<ItemRequestDtoForGet> expectedItemRequestDtoForGet =
                ReflectionTestUtils.invokeMethod(itemRequestService, "getListItemRequestDto", expectedItemRequest);

        assertEquals(expectedItemRequestDtoForGet, actualItemRequestDto);
        verify(itemRequestRepository, times(1)).findAllByRequesterId(anyInt());
    }

    @Test
    void getAllRequests() {
        List<ItemRequest> expectedItemRequest = new ArrayList<>();
        Page<ItemRequest> pageExpectedItemRequest = new PageImpl<>(expectedItemRequest);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(itemRequestRepository.findAll(pageRequest)).thenReturn(pageExpectedItemRequest);

        List<ItemRequestDtoForGet> actualItemRequestDto = itemRequestService.getAllRequests(1, pageRequest);

        assertEquals(expectedItemRequest, actualItemRequestDto);
        verify(itemRequestRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void getById_whenItemRequestFound_thenReturnedItemRequest() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        List<ItemDto> items = new ArrayList<>();
        ItemRequestDto itemRequestDtoToSaved = new ItemRequestDto(1, "name", LocalDateTime.now());
        ItemRequest itemRequestToSave = ItemRequestMapper.toItemRequest(user, itemRequestDtoToSaved);
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(UserMapper.toUserDto(user)));
        when(itemService.findAllByRequest(anyInt())).thenReturn(items);
        when(itemRequestRepository.getById(anyInt())).thenReturn(itemRequestToSave);

        ItemRequestDtoForGet actualItemRequestDto = itemRequestService.getById(userId,1).get();

        assertEquals(ItemRequestMapper.toItemRequestDtoForGet(itemRequestToSave, items), actualItemRequestDto);
        verify(itemRequestRepository, times(1)).getById(anyInt());
    }

    @Test
    void getById_whenItemRequestNotFound_thenNotFoundException() {
        when(itemRequestRepository.getById(anyInt())).thenThrow(EntityNotFoundException.class);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.getById(1, 1));
    }
}