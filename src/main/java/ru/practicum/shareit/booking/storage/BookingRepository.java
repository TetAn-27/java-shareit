package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Override
    Booking save(Booking booking);

    @Override
    Booking getById(Integer bookingId);

    List<Booking> findAllByBookerId(int userId);

    //List<Booking> findAllByOwnerId(int userId);
}
