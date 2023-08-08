package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public Optional<ItemRequestDto> create(Integer userId, ItemRequestDto itemRequestDto) {
        return Optional.empty();
    }

    @Override
    public List<ItemRequestDto> findAll(Integer userId) {
        return null;
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
