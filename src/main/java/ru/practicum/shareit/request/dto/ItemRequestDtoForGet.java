package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class ItemRequestDtoForGet {
    private Integer id;
    @NotNull
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
