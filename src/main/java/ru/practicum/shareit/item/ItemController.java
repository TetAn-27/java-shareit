package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Optional<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @Valid @RequestBody ItemDto itemDto) {
       return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId);
    }
}
