package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

    private UserDto makeUserDto(String email, String name) {
        return new UserDto(
                null,
                name,
                email
        );
    }

    @Test
    void integrationTestingFindAllUsers() {

        List<UserDto> sourceUsers = List.of(
                makeUserDto("name1@email", "name1"),
                makeUserDto("name2@email", "name2"),
                makeUserDto("name3@email", "name3")
        );

        for (UserDto user : sourceUsers) {
            User entity = UserMapper.toUser(user.getId(), user);
            em.persist(entity);
        }
        em.flush();

        List<UserDto> targetUsers = service.findAll();

        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (UserDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem( allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void integrationTestingSaveUser() {

        UserDto userDto = makeUserDto("name@email.com", "name");
        service.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void integrationTestingUpdateUser() {

        UserDto userDto = makeUserDto("name@email.com", "name");
        service.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        UserDto userDtoUpdate = makeUserDto("nameUpdate@email.com", "nameUpdate");
        service.update(user.getId(), userDtoUpdate);

        TypedQuery<User> queryUpdate = em.createQuery("Select u from User u where u.id = :id", User.class);
        User userUpdate = queryUpdate.setParameter("id", user.getId())
                .getSingleResult();

        assertThat(userUpdate.getId(), notNullValue());
        assertThat(userUpdate.getName(), equalTo(userDtoUpdate.getName()));
        assertThat(userUpdate.getEmail(), equalTo(userDtoUpdate.getEmail()));
    }

    @Test
    void integrationTestingGetUserById() {

        UserDto userDto = makeUserDto("name@email.com", "name");
        User entity = UserMapper.toUser(userDto.getId(), userDto);
        em.persist(entity);
        em.flush();

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        UserDto targetUser = service.getUserById(user.getId()).get();

        assertThat(targetUser.getId(), notNullValue());
        assertThat(targetUser.getName(), equalTo(userDto.getName()));
        assertThat(targetUser.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void integrationTestingDelete() {
        UserDto userDto = makeUserDto("name@email.com", "name");
        service.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        List<UserDto> targetUsers = service.findAll();
        assertThat(targetUsers.size(), equalTo(1));

        service.deleteUser(user.getId());

        targetUsers = service.findAll();
        assertThat(targetUsers.size(), equalTo(0));
    }
}