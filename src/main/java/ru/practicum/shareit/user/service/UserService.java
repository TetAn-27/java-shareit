package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();

    Optional<UserDto> create(UserDto userDto);

    Optional<UserDto> update(UserDto userDto);

    Optional<UserDto> getUserById(Integer userId);

    void deleteUser(Integer userId);
}
