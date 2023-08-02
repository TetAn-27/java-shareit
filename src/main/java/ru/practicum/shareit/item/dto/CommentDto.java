package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    @NotNull
    private String text;
    @NotNull
    private Integer itemId;
    private Integer authorId;
    private LocalDateTime created;
}
