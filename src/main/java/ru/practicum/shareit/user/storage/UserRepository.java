package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {
    void createUser(User user);
    boolean updateUser(User user);
    void deleteUser(Integer userId);
    User getUserById(Integer id);
    Collection<User> getUsers();
}
