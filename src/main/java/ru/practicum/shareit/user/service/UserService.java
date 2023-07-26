package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    Optional<UserDto> create(UserDto userDto);

    Optional<UserDto> update(Integer userId, UserDto userDto);

    Optional<UserDto> getUserById(Integer userId);

    //void deleteUser(Integer userId);
}
