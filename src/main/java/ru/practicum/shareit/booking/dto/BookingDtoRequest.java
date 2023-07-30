package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoRequest {

    private Integer id;
    @NotNull(message = "Значение старта не может быть пустым")
    @FutureOrPresent(message = "Значение старта не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Значение окончания не может быть пустым")
    @Future(message = "Значение окончания не может быть в прошлом")
    private LocalDateTime end;
    @NotNull(message = "Значение id вещи не может быть пустым")
    private Integer itemId;
    private Integer booker;
    private Status status;
}
