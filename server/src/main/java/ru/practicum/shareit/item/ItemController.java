package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.service.ItemService;

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
                              @RequestBody ItemDto itemDto) {
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
    public List<ItemDtoForGet> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam(value = "from", defaultValue = "0", required = false)
                                               Integer page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false)
                                                   Integer size) {
        return itemService.getAllUserItems(userId, PageRequest.of(page, size));
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItems(@RequestParam(value = "text") String text,
                                        @RequestParam(value = "from", defaultValue = "0", required = false)
                                        Integer page,
                                        @RequestParam(value = "size", defaultValue = "10", required = false)
                                            Integer size) {
        return itemService.searchForItems(text, PageRequest.of(page, size));
    }

    @PostMapping("/{itemId}/comment") //POST /items/{itemId}/comment
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("itemId") Integer itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto).get();
    }
}
