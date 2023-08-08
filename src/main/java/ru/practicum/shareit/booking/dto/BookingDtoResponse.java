package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoResponse {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoResponse item;
    private BookerDtoResponse booker;
    private Status status;

    @Data
    @AllArgsConstructor
    public static class ItemDtoResponse {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class BookerDtoResponse {
        private Integer id;
    }
}
