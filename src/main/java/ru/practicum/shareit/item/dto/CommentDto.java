package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
//@AllArgsConstructor
public class CommentDto {

    private Integer id;
    @NotNull
    @NotEmpty
    private String text;
    private Integer itemId;
    private Integer authorId;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(Integer id, String text, Integer itemId, Integer authorId, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.created = created == null ? LocalDateTime.now() : created;
    }
}
