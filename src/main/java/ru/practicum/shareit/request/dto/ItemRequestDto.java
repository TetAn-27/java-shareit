package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime created;

    public ItemRequestDto(Integer id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created == null ? LocalDateTime.now() : created;
    }

    /*public ItemRequestDto() {
    }*/
}
