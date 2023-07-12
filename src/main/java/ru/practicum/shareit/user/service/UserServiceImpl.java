package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userRepository.getUsers().size());
        return userRepository.getUsers();
    }

    @Override
    public User create(User user) {
        userRepository.createUser(user);
        return user;
    }

    @Override
    public User update(User user) {
        if (userRepository.updateUser(user)) {
            return user;
        }
        log.debug("Пользователь с id: {} не найден", user.getId());
        return null;

    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getUserById(id);
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteUser(userId);
    }
}
