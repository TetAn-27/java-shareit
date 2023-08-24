package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void findAll_whenRightConditions_thenReturnedList() {
        List<User> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUserDto = userService.findAll();

        assertEquals(UserMapper.toListUserDto(expectedUsers), actualUserDto);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_whenParametersValid_thenSavedUser() {
        User userToSave = new User(0, "name", "name@mail.ru");
        when(userRepository.save(userToSave)).thenReturn(userToSave);
        UserDto userDto = UserMapper.toUserDto(userToSave);

        UserDto actualUserDto = userService.create(userDto).get();

        assertEquals(userDto, actualUserDto);
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void updateUser_whenUserFound_thenUpdateOnlyAvailableFields() {
        Integer userId = 0;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("oldName");
        oldUser.setEmail("oldName@email.com");
        User newUser = new User();
        newUser.setName("newName");
        newUser.setEmail("newName@email.com");
        when(userRepository.getById(userId)).thenReturn(oldUser);
        when(userRepository.save(oldUser)).thenReturn(oldUser);

        userService.create(UserMapper.toUserDto(oldUser));
        UserDto actualUser = userService.update(userId, UserMapper.toUserDto(newUser)).get();

        verify(userRepository, times(2)).save(userArgumentCaptor.capture());
        User userSaved = userArgumentCaptor.getValue();

        assertEquals("newName", userSaved.getName());
        assertEquals("newName@email.com", userSaved.getEmail());
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() {
        Integer userId = 0;
        User expectedUser = new User();
        when(userRepository.getById(userId)).thenReturn(expectedUser);

        UserDto actualUserDto = userService.getUserById(userId).get();

        assertEquals(UserMapper.toUserDto(expectedUser), actualUserDto);
        verify(userRepository, times(1)).getById(userId);
    }

    @Test
    void getUserById_whenUserNotFound_thenNotFoundException() {
        Integer userId = 0;
        when(userRepository.getById(userId)).thenThrow(EntityNotFoundException.class);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
        () -> userService.getUserById(userId));
    }

    @Test
    void deleteUser_whenUserNotFound_thenNotFoundException() {
        Integer userId = 0;
        doThrow(NotFoundException.class).when(userRepository).deleteById(userId);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.deleteUser(userId));
    }
}