package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
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
        List<User> allUsers = userRepository.findAll();
        log.debug("Текущее количество пользователей: {}", allUsers.size());
        return toListUserDto(allUsers);
    }

    @Override
    public Optional<UserDto> create(UserDto userDto) {
        User user = UserMapper.toUser(0, userDto);
        return Optional.of(UserMapper.toUserDto(userRepository.save(user)));
    }

    @Override
    public Optional<UserDto> update(Integer userId, UserDto userDto) {
        User user = getUpdateUser(userId, UserMapper.toUser(userId, userDto));
        return Optional.of(UserMapper.toUserDto(userRepository.save(user)));

    }

    @Override
    public Optional<UserDto> getUserById(Integer id) {
        try {
            return Optional.of(UserMapper.toUserDto(userRepository.getById(id)));
        } catch (EntityNotFoundException ex) {
            log.error("User с ID {} не был найден", id);
            throw new NotFoundException("User с таким ID не был найден");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            userRepository.deleteById(userId);
        } catch (DataAccessException ex) {
            log.error("User с ID {} не был найден", userId);
            throw new NotFoundException("User с таким ID не был найден");
        }
    }

    private List<UserDto> toListUserDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }

    private User getUpdateUser(Integer userId, User userUpdate) {
        User user = userRepository.getById(userId);
        user.setName(userUpdate.getName() != null ? userUpdate.getName() : user.getName());
        user.setEmail(userUpdate.getEmail() != null ? userUpdate.getEmail() : user.getEmail());
        log.info("Пользователь {} был обновлен", user.getName());
        return user;
    }
}
