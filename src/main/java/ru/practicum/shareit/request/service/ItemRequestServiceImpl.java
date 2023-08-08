package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserService userService, ItemService itemService) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public Optional<ItemRequestDto> create(Integer userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userId, userService.getUserById(userId).get());
        return Optional.of(ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(user, itemRequestDto))));
    }

    @Override
    public List<ItemRequestDtoForGet> findAll(Integer userId) {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequester(userId);
        List<ItemRequestDtoForGet> requestsDto = new ArrayList<>();
        for (ItemRequest itemRequest : requests) {
            List<ItemDto> items = itemService.findAllByRequest;
            ItemRequestDtoForGet requestDto = ItemRequestMapper.toItemRequestDtoForGet(itemRequest, items);
            requestsDto.add(requestDto);
        }
        return requestsDto;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public Optional<ItemRequestDto> getItemRequestById(Integer requestId) {
        return Optional.empty();
    }
}
