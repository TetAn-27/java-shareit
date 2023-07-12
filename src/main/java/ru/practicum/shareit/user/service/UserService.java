package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User getUserById(Integer id);

    void deleteUser(Integer userId);
}
