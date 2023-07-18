package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserEmailException;
import ru.practicum.shareit.exception.UserIdException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        validationEmail(user);
        ++id;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Integer userId, User user) {
        if (users.containsKey(userId)) {
            validationEmail(user);
            users.put(userId, user);
            log.info("Пользователь {} был обновлен", user.getName());
            return user;
        } else {
            throw new NotFoundException("Пользовавтель с таким ID не был найден");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("Пользователь с ID {} был удален", userId);
        users.remove(userId);
    }

    @Override
    public User getUserById(Integer id) {
        validationId(id);
        return users.get(id);
    }

    @Override
    public boolean isContainsUserId(Integer userId) {
        return users.containsKey(userId);
    }

    private void validationId(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserIdException(String.format("Пользователя с id %s не существует", id));
        }
    }

    private void validationEmail(User user) {
        boolean isValidEmail = users.values().stream()
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
