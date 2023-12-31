package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class ItemRequestDtoForGet {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
