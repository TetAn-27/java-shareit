package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
    private ItemClient itemClient;
    @InjectMocks
    ItemController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto itemDto;
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
        when(itemClient.itemCreate(anyInt(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

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

        verify(itemClient).itemCreate(anyInt(), any());
    }

    @Test
    void updateItem_whenParametersValid_thenReturnedItem() throws Exception {
        int id = 1;
        when(itemClient.itemUpdate(anyInt(), anyInt(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

        String result = mvc.perform(patch("/items/{itemId}", id)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).itemUpdate(anyInt(), anyInt(), any());
    }

    @Test
    void getItemById_whenParametersValid_thenReturnedItem() throws Exception {
        int id = 1;
        when(itemClient.getItem(anyInt(), anyInt()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));
        String result = mvc.perform(get("/items/{itemId}", id)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).getItem(id, id);
    }

    @Test
    void getAllUserItems_whenParametersValid_thenReturnedItemList() throws Exception {
        int id = 1;
        when(itemClient.getAllUserItems(id, 0, 10))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));
        String result = mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient).getAllUserItems(id, 0, 10);
    }

    @Test
    void searchForItems_whenTextNull_thenBadRequest() throws Exception {
        String result = mvc.perform(get("/items/search"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient, never()).searchForItems(1, "text", 0, 10);
    }

    @Test
    void addComment_whenParametersValid_thenReturnedComment() throws Exception {
        int id = 1;
        when(itemClient.addComment(anyInt(), anyInt(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(Object.class)));

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

        verify(itemClient).addComment(anyInt(), anyInt(), any());
    }
}
