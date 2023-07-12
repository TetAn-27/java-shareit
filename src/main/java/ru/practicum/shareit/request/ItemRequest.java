package ru.practicum.shareit.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    @Size(max = 200, message = "Описание фильма превышает 200 символов")
    private String description;
    private int requester;
    private LocalDateTime created;
}
