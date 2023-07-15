package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    User createUser(User user);
    User updateUser(Integer userId, User user);
    void deleteUser(Integer userId);
    User getUserById(Integer id);
    List<User> getUsers();
}
