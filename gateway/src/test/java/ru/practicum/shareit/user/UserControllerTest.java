package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserClient userClient;
    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDto = new UserDto(
                1,
                "name",
                "name@mail.com");
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() throws Exception {
        int id = 1;
        when(userClient.getUser(anyInt()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));
        String result = mvc.perform(get("/users/{userId}", id)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).getUser(id);
    }

    @Test
    void findAll_whenParametersValid_thenReturnedUser() throws Exception {
        int id = 1;
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));
        String result = mvc.perform(get("/users")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).getAllUsers();
    }

    @Test
    void create_whenParametersValid_thenReturnedUser() throws Exception {
        when(userClient.userCreate(any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).userCreate(any());
    }

    @Test
    void update_whenUserFound_thenReturnedUser() throws Exception {
        int id = 1;
        when(userClient.userUpdate(anyInt(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(patch("/users/{usersId}", id)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).userUpdate(anyInt(),  any());
    }
}
