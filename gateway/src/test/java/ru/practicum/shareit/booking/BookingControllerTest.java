package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;
    @InjectMocks
    BookingController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private BookItemRequestDto bookItemRequestDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        bookItemRequestDto = new BookItemRequestDto(
                1,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(50));

        mapper.findAndRegisterModules();
    }

    @Test
    void responseToRequest_whenParametersValid_thenReturnedBooking() throws Exception {
        int id = 1;
        when(bookingClient.bookItemUpdate(anyInt(), anyInt(), eq(true)))
                .thenReturn(ResponseEntity.of(Optional.of(BookItemRequestDto.class)));

        String result = mvc.perform(patch("/bookings/{bookingId}?approved=true", id)
                        .content(mapper.writeValueAsString(bookItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingClient).bookItemUpdate(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    void getBookingById_whenParametersValid_thenReturnedBooking() throws Exception {
        int id = 1;
        when(bookingClient.getBooking(anyInt(), anyInt()))
                .thenReturn(ResponseEntity.of(Optional.of(BookItemRequestDto.class)));

        String result = mvc.perform(get("/bookings/{bookingId}", id)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingClient).getBooking(anyInt(), anyInt());
    }

    @Test
    void getAllBookingByBookerId_whenParametersValid_thenReturnedBookingList() throws Exception {
        int id = 1;
        when(bookingClient.getBookings(1, BookingState.valueOf("WAITING"), 0, 10))
                .thenReturn(ResponseEntity.of(Optional.of(BookItemRequestDto.class)));

        String result = mvc.perform(get("/bookings?state=WAITING")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingClient).getBookings(1, BookingState.valueOf("WAITING"), 0, 10);
    }

    @Test
    void getAllBookingByOwnerId_whenParametersValid_thenReturnedBookingList() throws Exception {
        int id = 1;
        when(bookingClient.getBookingsOwner(1, BookingState.valueOf("WAITING"), 0, 10))
                .thenReturn(ResponseEntity.of(Optional.of(BookItemRequestDto.class)));

        String result = mvc.perform(get("/bookings/owner?state=WAITING")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingClient).getBookingsOwner(1, BookingState.valueOf("WAITING"), 0, 10);
    }
}
