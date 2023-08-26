/*
package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;
    @InjectMocks
    BookingController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private BookingDtoResponse bookingDtoResponse;
    private BookingDtoRequest bookingDtoRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        bookingDtoResponse = new BookingDtoResponse(
                1,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(50),
                new BookingDtoResponse.ItemDtoResponse(1, "name"),
                new BookingDtoResponse.BookerDtoResponse(1),
                Status.WAITING);
        bookingDtoRequest = new BookingDtoRequest(
                1,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(50),
                1,
                1,
                Status.WAITING);
        mapper.findAndRegisterModules();
    }

    @Test
    void createRequest_whenParametersValid_thenReturnedBooking() throws Exception {
        when(bookingService.createRequest(1, bookingDtoRequest))
                .thenReturn(Optional.of(bookingDtoResponse));

        String result = mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDtoResponse), result);
    }

    @Test
    void responseToRequest_whenParametersValid_thenReturnedBooking() throws Exception {
        int id = 1;
        when(bookingService.responseToRequest(anyInt(), anyInt(), eq(true)))
                .thenReturn(Optional.of(bookingDtoResponse));

        String result = mvc.perform(patch("/bookings/{bookingId}?approved=true", id)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDtoResponse), result);
    }

    @Test
    void getBookingById_whenParametersValid_thenReturnedBooking() throws Exception {
        int id = 1;
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(Optional.of(bookingDtoResponse));

        String result = mvc.perform(get("/bookings/{bookingId}", id)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDtoResponse), result);
        verify(bookingService).getBookingById(anyInt(), anyInt());
    }

    @Test
    void getAllBookingByBookerId_whenParametersValid_thenReturnedBookingList() throws Exception {
        int id = 1;
        when(bookingService.getAllBookingByBookerId(1, "WAITING",
                PageRequest.of(0, 10, Sort.Direction.DESC, "start")))
                .thenReturn(List.of(bookingDtoResponse));

        String result = mvc.perform(get("/bookings?state=WAITING")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).getAllBookingByBookerId(1, "WAITING",
                PageRequest.of(0, 10, Sort.Direction.DESC, "start"));
    }

    @Test
    void getAllBookingByOwnerId_whenParametersValid_thenReturnedBookingList() throws Exception {
        int id = 1;
        when(bookingService.getAllBookingByOwnerId(1, "WAITING",
                PageRequest.of(0, 10, Sort.Direction.DESC, "start")))
                .thenReturn(List.of(bookingDtoResponse));

        String result = mvc.perform(get("/bookings/owner?state=WAITING")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(bookingDtoResponse)), result);
        verify(bookingService).getAllBookingByOwnerId(1, "WAITING",
                PageRequest.of(0, 10, Sort.Direction.DESC, "start"));
    }
}*/
