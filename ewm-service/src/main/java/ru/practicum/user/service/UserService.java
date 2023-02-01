package ru.practicum.user.service;

import ru.practicum.user.dto.UserResponse;
import ru.practicum.user.entity.User;

import java.util.List;

public interface UserService {
    /**
     * Method returns all users (if no requirements) or select users is per ids array.
     *
     * @param ids IDs of users to select.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of all users.
     */
    List<UserResponse> getUsers(Long[] ids, Integer from, Integer size);

    /**
     * Method adds user to repository.
     *
     * @param user User to be added.
     *
     * @return Added user with assigned ID.
     */
    UserResponse createUser(User user);

    /**
     * Method deletes user in repository.
     *
     * @param id ID of user to be deleted.
     */
    void deleteUser(Long id);
}
