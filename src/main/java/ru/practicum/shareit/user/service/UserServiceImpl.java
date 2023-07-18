package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAll() {
        List<User> allUsers = userRepository.getUsers();
        log.debug("Текущее количество пользователей: {}", allUsers.size());
        return toListUserDto(allUsers);
    }

    @Override
    public Optional<UserDto> create(UserDto userDto) {
        return Optional.of(UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(0, userDto))));
    }

    @Override
    public Optional<UserDto> update(Integer userId, UserDto userDto) {
        return Optional.of(UserMapper.toUserDto(userRepository.updateUser(userId,
                UserMapper.toUserForUpdate(userId, userDto, userRepository.getUserById(userId)))));

    }

    @Override
    public Optional<UserDto> getUserById(Integer id) {
        return Optional.of(UserMapper.toUserDto(userRepository.getUserById(id)));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteUser(userId);
    }

    private List<UserDto> toListUserDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }
}
