package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.NonNull;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(int userId, UserDto userDto) {
        return new User(
                userId,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static User toUserForUpdate(int userId, UserDto userDto, User user) {
        return new User(
                userId,
                userDto.getName() != null ? userDto.getName() :user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() :user.getEmail()
        );
    }
}
