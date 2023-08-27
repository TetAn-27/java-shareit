package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestClient itemRequestClient;
    @InjectMocks
    ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        mapper.findAndRegisterModules();
        itemRequestDto = new ItemRequestDto(
                1,
                "description",
                LocalDateTime.now());
    }

    @Test
    void create_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestClient.createItemRequest(anyInt(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestClient).createItemRequest(anyInt(), any());
    }

    @Test
    void findAll_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestClient.getAll(anyInt()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestClient).getAll(anyInt());
    }

    @Test
    void getAllRequests_whenParametersValid_thenReturnedItemRequestList() throws Exception {
        when(itemRequestClient.getAllItemRequest(1, 0, 10))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestClient).getAllItemRequest(1,0, 10);
    }

    @Test
    void getById_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestClient.getItemRequest(anyInt(), anyInt()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestClient).getItemRequest(anyInt(), anyInt());
    }
}
