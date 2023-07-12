package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Item {
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма превышает 200 символов")
    private String description;
    private boolean available;
    private int owner;
    private ItemRequest request;
}
