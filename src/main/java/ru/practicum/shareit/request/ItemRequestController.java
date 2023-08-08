package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto).get();
    }

    @GetMapping
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.findAll(userId);
    }

    @GetMapping("/all") //GET /requests/all?from={from}&size={size}
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam(value = "from", defaultValue = "0", required = false)
                                                    Integer from,
                                               @RequestParam(value = "size", defaultValue = "10", required = false)
                                                   Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}") //GET /requests/{requestId}
    public ItemRequestDto getItemRequestById(@PathVariable("requestId") Integer id) {
        return itemRequestService.getItemRequestById(id).get();
    }
}
