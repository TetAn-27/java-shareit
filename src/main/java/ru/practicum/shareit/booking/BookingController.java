package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoResponse createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        return bookingService.createRequest(userId, bookingDtoRequest).get();
    }

    @PatchMapping("/{bookingId}") //PATCH /bookings/{bookingId}?approved={approved}
    public BookingDtoResponse responseToRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @PathVariable("bookingId") Integer bookingId,
                                               @RequestParam(value = "approved") Boolean approved) {
        return bookingService.responseToRequest(userId, bookingId, approved).get();
    }

    @GetMapping("/{bookingId}") //GET /bookings/{bookingId}
    public BookingDtoResponse getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable("bookingId") Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId).get();
    }

    @GetMapping //GET /bookings?state={state}
    @Validated
    public List<BookingDtoResponse> getAllBookingByBookerId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                            @RequestParam(value = "state", defaultValue = "ALL",
                                                                    required = false) String state,
                                                            @RequestParam(value = "from", defaultValue = "0",
                                                                    required = false) @Min(0) int page,
                                                            @RequestParam(value = "size", defaultValue = "10",
                                                                    required = false) @Min(1) int size) {
        return bookingService.getAllBookingByBookerId(userId, state, PageRequest.of(page/size, size, Sort.Direction.DESC, "start"));
    }

    @GetMapping("/owner") //GET /bookings/owner?state={state}
    @Validated
    public List<BookingDtoResponse> getAllBookingByOwnerId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL",
                                                                   required = false) String state,
                                                           @RequestParam(value = "from", defaultValue = "0",
                                                                   required = false) @Min(0) int page,
                                                           @RequestParam(value = "size", defaultValue = "10",
                                                                   required = false) @Min(1) int size) {
        return bookingService.getAllBookingByOwnerId(userId, state, PageRequest.of(page/size, size, Sort.Direction.DESC, "start"));
    }
}
