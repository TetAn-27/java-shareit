package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {

    public User(int id, String name, @Email @NonNull @NotEmpty String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private int id;

    private String name;

    @Email(message = "Некорректный email")
    @NotEmpty(message = "Email не может быть пустым")
    @NonNull
    private String email;
}
