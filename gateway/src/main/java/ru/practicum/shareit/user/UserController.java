package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Integer id) {
        log.info("Get user {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.userCreate(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable("userId") Integer userId,
                                         @RequestBody UserDto userDto) {
        log.info("Updating user userId={}", userId);
        return userClient.userUpdate(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Integer userId) {
        log.info("Remove user userId={}", userId);
        return userClient.userDelete(userId);
    }
}
