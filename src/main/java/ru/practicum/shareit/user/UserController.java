package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        User updatedUser = userService.update(user);
        if (updatedUser == null) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        log.info("Received request to delete user with id={}", userId);
        userService.deleteUser(userId);
    }
}
