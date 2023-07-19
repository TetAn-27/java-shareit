package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Item {
    private Integer id;
    @NotNull
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание фильма превышает 200 символов")
    private String description;
    @NotNull
    private Boolean available;
    private final User owner;
    private ItemRequest request;
}
