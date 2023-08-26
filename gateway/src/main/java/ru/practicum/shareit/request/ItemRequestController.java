package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping()
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getAll(userId);
    }

    @GetMapping("/all") //GET /requests/all?from={from}&size={size}
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @RequestParam(value = "from", defaultValue = "0", required = false)
                                                     Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false)
                                                     Integer size) {
        return itemRequestClient.getAllItemRequest(userId, page, size);
    }

    @GetMapping("/{requestId}") //GET /requests/{requestId}
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @PathVariable("requestId") Integer id) {
        return itemRequestClient.getItemRequest(userId, id);
    }
}
