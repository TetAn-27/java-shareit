package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    private ItemDto makeItemDto(String name, String description, Boolean available, Integer requestId) {
        return new ItemDto(
                null,
                name,
                description,
                available,
                requestId

        );
    }

    @Test
    void update() {
        UserDto userDto = new UserDto(
                1,
                "name",
                "name@email.com"
        );
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1,
                "description",
                null
        );
        userService.create(userDto);
        itemRequestService.create(userDto.getId(), itemRequestDto);
        User userMap = UserMapper.toUser(userDto.getId(), userDto);
        ItemRequest itemRequestMap = ItemRequestMapper.toItemRequest(userMap, itemRequestDto);
        ItemDto itemDto = new ItemDto(
                null,
                "name1",
                "description1",
                true,
                1);

        itemService.create(userDto.getId(), itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item item = query.setParameter("name", itemDto.getName())
                .getSingleResult();
        int itemId = item.getId();

        ItemDto itemDtoUpdate = makeItemDto("nameUpdate", "descriptionUpdate", false, 1);
        itemService.update(userDto.getId(), itemId, itemDtoUpdate);

        TypedQuery<Item> queryUpdate = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemUpdate = queryUpdate.setParameter("id", itemId)
                .getSingleResult();

        assertThat(itemUpdate.getId(), notNullValue());
        assertThat(itemUpdate.getName(), equalTo(itemDtoUpdate.getName()));
        assertThat(itemUpdate.getDescription(), equalTo(itemDtoUpdate.getDescription()));
        assertThat(itemUpdate.getAvailable(), equalTo(itemDtoUpdate.getAvailable()));
        assertThat(itemUpdate.getRequest().getId(), equalTo(itemDtoUpdate.getRequestId()));
        assertThat(itemUpdate.getOwner().getId(), equalTo(userDto.getId()));
    }
}