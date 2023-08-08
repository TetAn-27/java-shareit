package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
    Optional<ItemRequestDto> create(Integer userId, ItemRequestDto itemRequestDto);
    List<ItemRequestDto> findAll(Integer userId);
    List<ItemRequestDto> getAllRequests (Integer userId, Integer from, Integer size);
    Optional<ItemRequestDto> getItemRequestById(Integer requestId);
}
