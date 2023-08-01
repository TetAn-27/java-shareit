package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Override
    Booking save(Booking booking);

    @Override
    Booking getById(Integer bookingId);

    List<Booking> findAllByBookerId(int userId);

    List<Booking> findAllByItemOwnerId(int userId);
    //List<Booking> findAllByItemId(int itemId);

    Booking findFirst1ByItemIdAndStartLessThanEqualAndStatusOrderByStartDesc(
            Integer itemId, LocalDateTime now, Status status);

    Booking findFirst1ByItemIdAndStartGreaterThanEqualAndStatusOrderByStartAsc(
            Integer itemId, LocalDateTime now, Status status);
}
