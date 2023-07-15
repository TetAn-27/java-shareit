package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public Optional<Item> createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Optional<Item> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @Valid @RequestBody ItemDto itemDto) {
       return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Optional<Item> getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping()
    public List<Item> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/items/search")
    public List<Item> searchForItems(@RequestParam(value = "text") String text) {
        return itemService.searchForItems(text);
    }
}
