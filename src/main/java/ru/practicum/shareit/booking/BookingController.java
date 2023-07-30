package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createRequest(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createRequest(bookingDto).get();
    }

    @PatchMapping("/{bookingId}") //PATCH /bookings/{bookingId}?approved={approved}
    public BookingDto resposeToRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @PathVariable("bookingId") Integer bookingId,
                                       @RequestParam(value = "approved") Boolean approved,
                                       @RequestBody BookingDto bookingDto) {
        return bookingService.resposeToRequest(userId, bookingId, approved, bookingDto).get();
    }

    @GetMapping("/{bookingId}") //GET /bookings/{bookingId}
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable("bookingId") Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId).get();
    }

    @GetMapping //GET /bookings?state={state}
    public List<BookingDto> getAllBookingByBookerId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                    @RequestParam(value = "state", defaultValue = "ALL", required = false)
                                                Status state) {
        return bookingService.getAllBookingByBookerId(userId, state);
    }

   /* @GetMapping("/owner") //GET /bookings/owner?state={state}
    public List<BookingDto> getAllBookingByOwnerId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @RequestParam(value = "state", defaultValue = "ALL", required = false)
                                              Status state) {
        return bookingService.getAllBookingByOwnerId(userId, state);
    }*/
}
