package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User save(User user);

    //void delete(Integer userId);

    User getById(Integer userId);

    List<User> findAll();

    //boolean isContainsUserId(Integer userId);
}
