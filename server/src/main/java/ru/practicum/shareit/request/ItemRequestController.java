package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForGet;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto).get();
    }

    @GetMapping()
    public List<ItemRequestDtoForGet> findAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.findAll(userId);
    }

    @GetMapping("/all") //GET /requests/all?from={from}&size={size}
    public List<ItemRequestDtoForGet> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @RequestParam(value = "from", defaultValue = "0", required = false)
                                                     Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false)
                                                         Integer size) {
        return itemRequestService.getAllRequests(userId, PageRequest.of(page, size));
    }

    @GetMapping("/{requestId}") //GET /requests/{requestId}
    public ItemRequestDtoForGet getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @PathVariable("requestId") Integer id) {
        return itemRequestService.getById(userId, id).get();
    }
}
