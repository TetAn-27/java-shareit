package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserEmailException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        List<User> allUsers = userRepository.findAll();
        log.debug("Текущее количество пользователей: {}", allUsers.size());
        return toListUserDto(allUsers);
    }

    @Override
    public Optional<UserDto> create(UserDto userDto) {
        User user = UserMapper.toUser(0, userDto);
        validationEmail(user);
        return Optional.of(UserMapper.toUserDto(userRepository.save(user)));
    }

    @Override
    public Optional<UserDto> update(Integer userId, UserDto userDto) {
        User user = UserMapper.toUser(userId, userDto);
        validationEmail(user);
        return Optional.of(UserMapper.toUserDto(userRepository.save(user)));

    }

    @Override
    public Optional<UserDto> getUserById(Integer id) {
        return Optional.of(UserMapper.toUserDto(userRepository.getById(id)));
    }

    /*@Override
    public void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }*/

    private List<UserDto> toListUserDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }

    private void validationEmail(User user) {
        boolean isValidEmail = findAll().stream()
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .anyMatch(u -> Objects.equals(user.getEmail(), u.getEmail()));
        if (isValidEmail) {
            throw new UserEmailException(String.format(
                    "Пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()
            ));
        }
    }
}
