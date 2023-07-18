package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public Optional<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @RequestBody ItemDto itemDto) {
       return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping()
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItems(@RequestParam(value = "text") String text) {
        return itemService.searchForItems(text);
    }
}
