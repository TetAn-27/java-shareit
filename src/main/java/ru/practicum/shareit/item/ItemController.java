package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto).get();
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @RequestBody ItemDto itemDto) {
       return itemService.update(userId, itemId, itemDto).get();
    }

    @GetMapping("/{itemId}")
    public ItemDtoForGet getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(userId, itemId).get();
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
