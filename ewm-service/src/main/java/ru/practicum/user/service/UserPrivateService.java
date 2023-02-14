package ru.practicum.user.service;

import ru.practicum.user.dto.UserResponseIdAndName;

import java.util.List;

public interface UserPrivateService {
    /**
     * Method returns users rating.
     *
     * @param userId ID of user requesting rating.
     * @param count Number of users in rating.
     *
     * @return List of users.
     */
    List<UserResponseIdAndName> getUsersRating(Long userId, Integer count);
}
