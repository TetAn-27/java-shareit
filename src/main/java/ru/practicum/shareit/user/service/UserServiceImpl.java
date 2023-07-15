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
    public List<User> findAll() {
        List<User> allUsers = userRepository.getUsers();
        log.debug("Текущее количество пользователей: {}", allUsers.size());
        return allUsers;
    }

    @Override
    public Optional<User> create(UserDto userDto) {
        //userRepository.createUser(UserMapper.toUser(userDto));
        return Optional.of(userRepository.createUser(UserMapper.toUser(0, userDto)));
    }

    @Override
    public Optional<User> update(Integer userId, UserDto userDto) {
        return Optional.of(userRepository.updateUser(userId,
                UserMapper.toUserForUpdate(userId, userDto, getUserById(userId).get())));

    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.of(userRepository.getUserById(id));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteUser(userId);
    }

    /*private List<UserDto> toListUserDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }*/
}
