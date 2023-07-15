package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> create(UserDto userDto);

    Optional<User> update(Integer userId, UserDto userDto);

    Optional<User> getUserById(Integer userId);

    void deleteUser(Integer userId);
}
