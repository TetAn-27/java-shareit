package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getId(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto, Item item, User user) {
        return new Comment(
                commentDto.getId() != null ? commentDto.getId() : 0,
                commentDto.getText(),
                item,
                user,
                commentDto.getCreated()
        );
    }
}
