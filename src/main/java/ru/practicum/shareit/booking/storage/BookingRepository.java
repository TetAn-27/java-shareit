package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Override
    Booking save(Booking booking);

    @Override
    Booking getById(Integer bookingId);

    Page<Booking> findAllByBookerId(int userId, Pageable page);

    Page<Booking> findAllByItemOwnerId(int userId, Pageable page);

    Booking findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
            Integer itemId, LocalDateTime now, Status status);

    Booking findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
            Integer itemId, LocalDateTime now, Status status);

    Booking findFirst1ByItemIdAndBookerId(int itemId, int bookerId);
}
