package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForGet;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
    Optional<ItemRequestDto> create(Integer userId, ItemRequestDto itemRequestDto);
    List<ItemRequestDtoForGet> findAll(Integer userId);
    List<ItemRequestDtoForGet> getAllRequests (Integer userId, PageRequest pageRequest);
    Optional<ItemRequestDtoForGet> getById(Integer userId, Integer requestId);
}
