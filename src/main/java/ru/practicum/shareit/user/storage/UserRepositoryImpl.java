package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.exception.UserEmailException;
import ru.practicum.shareit.exception.UserIdException;
import ru.practicum.shareit.user.User;

import java.util.*;

public class UserRepositoryImpl implements UserRepository{
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void createUser(User user) {
        for (User u : getUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new UserEmailException(String.format(
                        "Пользователь с электронной почтой %s уже зарегистрирован.",
                        user.getEmail()
                ));
            }
        }
        user.setId(getId());
        users.put(user.getId(), user);
    }

    @Override
    public boolean updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public void deleteUser(Integer userId) {
        users.remove(userId);
    }

    @Override
    public User getUserById(Integer id) {
        validationId(id);
        return users.get(id);
    }

    private int getId() {
        return ++id;
    }

    public void validationId(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserIdException(String.format("Пользователя с id %s не существует", id));
        }
    }
}
