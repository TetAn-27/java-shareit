package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    ItemController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemDtoForGet itemDtoForGet;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(
                1,
                "name",
                "description",
                true,
                1);
        itemDtoForGet = new ItemDtoForGet(
                1,
                "name",
                "description",
                true,
                1,
                new BookingDtoRequest(
                        1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(20),
                        1,
                        1,
                        Status.WAITING
                ),
                new BookingDtoRequest(
                        1,
                        LocalDateTime.now().plusMinutes(50),
                        LocalDateTime.now().plusMinutes(70),
                        2,
                        1,
                        Status.WAITING
                ),
                new ArrayList<>());
        commentDto = new CommentDto(
                1,
                "text",
                1,
                1,
                "name",
                LocalDateTime.now()
        );
        mapper.findAndRegisterModules();
    }
    @Test
    void createItem_whenParametersValid_thenReturnedItem() throws Exception {
        when(itemService.create(anyInt(), any()))
                .thenReturn(Optional.of(itemDto));

        String result = mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @Test
    void updateItem_whenParametersValid_thenReturnedItem() throws Exception {
        when(itemService.create(anyInt(), any()))
                .thenReturn(Optional.of(itemDto));

        String result = mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @Test
    void getItemById_whenParametersValid_thenReturnedItem() throws Exception {
        int id = 1;
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(Optional.of(itemDtoForGet));
        String result = mvc.perform(get("/items/{itemId}", id)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDtoForGet), result);
        verify(itemService).getItemById(id, id);
    }

    @Test
    void getAllUserItems_whenParametersValid_thenReturnedItemList() throws Exception {
        int id = 1;
        when(itemService.getAllUserItems(id, PageRequest.of(0, 10)))
                .thenReturn(List.of(itemDtoForGet));
        String result = mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemDtoForGet)), result);
        verify(itemService).getAllUserItems(id, PageRequest.of(0, 10));
    }

    @Test
    void searchForItems__whenParametersValid_thenReturnedItem() throws Exception {
        when(itemService.searchForItems("text", PageRequest.of(0, 10)))
                .thenReturn(List.of(itemDto));
        String result = mvc.perform(get("/items/search?text=text"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemDto)), result);
        verify(itemService).searchForItems("text", PageRequest.of(0, 10));
    }

    @Test
    void addComment_whenParametersValid_thenReturnedComment() throws Exception {
        int id = 1;
        when(itemService.addComment(anyInt(), anyInt(), any()))
                .thenReturn(Optional.of(commentDto));

        String result = mvc.perform(post("/items/{itemId}/comment", id)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(commentDto), result);
    }
}