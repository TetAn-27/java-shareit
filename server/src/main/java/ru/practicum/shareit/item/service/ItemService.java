package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(int userId, ItemDto itemDto);

    Optional<ItemDto>  update(int userId, Integer itemId, ItemDto itemDto);

    Optional<ItemDtoForGet> getItemById(int userId, int itemId);

    List<ItemDtoForGet> getAllUserItems(int userId, PageRequest pageRequest);

    List<ItemDto> searchForItems(String text, PageRequest pageRequest);

    Item getById(int itemId);

    Optional<CommentDto> addComment(Integer userId, Integer itemId, CommentDto commentDto);

    List<ItemDto> findAllByRequest(int userId);
}
