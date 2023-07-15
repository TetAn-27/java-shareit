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
                                       @Valid @RequestBody ItemDto item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @Valid @RequestBody Item item) {
       return itemService.update(userId, item);
    }
}
