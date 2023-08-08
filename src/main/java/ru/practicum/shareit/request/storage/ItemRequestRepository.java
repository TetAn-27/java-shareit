package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    @Override
    ItemRequest save(ItemRequest itemRequest);

    List<ItemRequest> findAllByRequester(Integer userId);
}
