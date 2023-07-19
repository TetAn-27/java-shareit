package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    private String name;
    @Email(message = "Некорректный email")
    @NotEmpty(message = "Email не может быть пустым")
    private String email;
}
