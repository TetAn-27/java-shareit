package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForGet;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private ItemRequestService itemRequestService;
    @InjectMocks
    ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoForGet itemRequestDtoForGet;

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

        itemRequestDtoForGet = new ItemRequestDtoForGet(
                1,
                "description",
                LocalDateTime.now(),
                new ArrayList<>());
    }

    @Test
    void create_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestService.create(anyInt(), any()))
                .thenReturn(Optional.of(itemRequestDto));

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

        assertEquals(mapper.writeValueAsString(itemRequestDto), result);
    }

    @Test
    void findAll_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestService.findAll(anyInt()))
                .thenReturn(List.of(itemRequestDtoForGet));

        String result = mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemRequestDtoForGet)), result);
        verify(itemRequestService).findAll(anyInt());
    }

    @Test
    void getAllRequests_whenParametersValid_thenReturnedItemRequestList() throws Exception {
        when(itemRequestService.getAllRequests(1, PageRequest.of(0, 10)))
                .thenReturn(List.of(itemRequestDtoForGet));

        String result = mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemRequestDtoForGet)), result);
        verify(itemRequestService).getAllRequests(1, PageRequest.of(0, 10));
    }

    @Test
    void getById_whenParametersValid_thenReturnedItemRequest() throws Exception {
        when(itemRequestService.getById(anyInt(), anyInt()))
                .thenReturn(Optional.of(itemRequestDtoForGet));

        String result = mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequestDtoForGet), result);
        verify(itemRequestService).getById(anyInt(), anyInt());
    }
}