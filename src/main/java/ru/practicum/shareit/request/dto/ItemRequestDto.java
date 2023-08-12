package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Integer id;
    @NotNull
    private String description;
    private LocalDateTime created;

    public ItemRequestDto(Integer id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created == null ? LocalDateTime.now() : created;
    }
}
