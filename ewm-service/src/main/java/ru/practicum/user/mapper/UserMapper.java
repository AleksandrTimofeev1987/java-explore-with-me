package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserCreate;
import ru.practicum.user.dto.UserResponse;
import ru.practicum.user.dto.UserResponseIdAndName;
import ru.practicum.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponseDto(User user);

    UserResponseIdAndName toUserResponseIdAndName(User user);

    User toUserEntity(UserCreate userDto);
}
