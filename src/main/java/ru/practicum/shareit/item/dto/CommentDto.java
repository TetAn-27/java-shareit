package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    @NotNull
    @NotEmpty
    private String text;
    private Integer itemId;
    private Integer authorId;
    private String authorName;
    private LocalDateTime created;
}
