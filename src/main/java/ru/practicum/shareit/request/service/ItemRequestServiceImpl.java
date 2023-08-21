package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        userService.getUserById(userId);
        return getListItemRequestDto(findAllByRequesterId(userId));
    }

    @Override
    public List<ItemRequestDtoForGet> getAllRequests(Integer userId, PageRequest pageRequestMethod) {
        Pageable page = pageRequestMethod;
        do {
            Page<ItemRequest> pageRequest = itemRequestRepository.findAll(page);
            pageRequest.getContent().forEach(ItemRequest -> {
            });
            if (pageRequest.hasNext()) {
                page = PageRequest.of(pageRequest.getNumber() + 1, pageRequest.getSize(), pageRequest.getSort());
                page = null;
            }
            List<ItemRequest> content = pageRequest.getContent().stream()
                    .filter(i -> !i.getRequester().getId().equals(userId))
                    .collect(Collectors.toList());
            return getListItemRequestDto(content);
        } while (page != null);
    }

    @Override
    public Optional<ItemRequestDtoForGet> getById(Integer userId, Integer requestId) {
        userService.getUserById(userId);
        List<ItemDto> items = itemService.findAllByRequest(requestId);
        try {
            return Optional.of(ItemRequestMapper.toItemRequestDtoForGet(
                    itemRequestRepository.getById(requestId), items));
        } catch (EntityNotFoundException ex) {
            log.error("Запроса с id {} не существует", requestId);
            throw new NotFoundException("Запроса с твким id не существует");
        }
    }

    private List<ItemRequestDtoForGet> getListItemRequestDto(List<ItemRequest> itemRequests) {
        List<ItemRequestDtoForGet> requestsDto = itemRequests.stream()
                .map(itemRequest -> {
                    List<ItemDto> items = itemService.findAllByRequest(itemRequest.getId());
                    ItemRequestDtoForGet requestDto = ItemRequestMapper.toItemRequestDtoForGet(itemRequest, items);
                    return requestDto;
                })
                .sorted((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()))
                .collect(Collectors.toList());
        return requestsDto;
    }

    private List<ItemRequest> findAllByRequesterId(Integer userId) {
        try {
            return itemRequestRepository.findAllByRequesterId(userId);
        } catch (Throwable ex) {
            log.info("Список запросов пользователя пуст");
            return new ArrayList<>();
        }
    }
}
