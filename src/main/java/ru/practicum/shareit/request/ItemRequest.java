package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Integer id;
    @Size(max = 200, message = "Описание фильма превышает 200 символов")
    private String description;
    private User requester;
    private LocalDateTime created;
}
