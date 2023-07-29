package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
