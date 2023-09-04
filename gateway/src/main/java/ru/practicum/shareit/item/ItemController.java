package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping()
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        return itemClient.itemCreate(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable("itemId") Integer itemId,
                              @RequestBody ItemDto itemDto) {
        return itemClient.itemUpdate(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam(value = "from", defaultValue = "0", required = false)
                                               Integer page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false)
                                               Integer size) {
        return itemClient.getAllUserItems(userId, page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchForItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(value = "text") String text,
                                                 @RequestParam(value = "from", defaultValue = "0", required = false)
                                                     Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10", required = false)
                                                     Integer size) {
        return itemClient.searchForItems(userId, text, page, size);
    }

    @PostMapping("/{itemId}/comment") //POST /items/{itemId}/comment
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("itemId") Integer itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
