package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private Integer id;
    private String name;
    @Email
    @NotEmpty
    @Column(name = "email", nullable = false)
    private String email;
}
