package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Override
    User save(User user);

    @Override
    void deleteById(Integer userId);

    @Override
    User getById(Integer userId);

    @Override
    List<User> findAll();
}
