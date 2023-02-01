package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.user.dto.UserResponse;
import ru.practicum.user.entity.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final UserRepository repository;
    private final UserMapper mapper;


    @Override
    public List<UserResponse> getUsers(Long[] ids, Integer from, Integer size) {
        log.debug("A list of users is requested with the following pagination parameters: from={} and size={}.", from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
        List<User> foundUsers;
        if (ids == null) {
            foundUsers = repository.findAll(page).getContent();
        } else {
            foundUsers = repository.findUsersById(ids, page);
        }

        log.debug("A list of users is received from repository with size of {}.", foundUsers.size());
        return foundUsers
                .stream()
                .map(user -> mapper.toUserResponseDto(user))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse createUser(User user) {
        log.debug("Request to add user with name={} is received.", user.getName());

        User createdUser;
        try {
            createdUser = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("User email is a duplicate.");
        }

        log.debug("User with ID={} is added to repository.", createdUser.getId());
        return mapper.toUserResponseDto(createdUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Request to delete user with ID={} is received.", id);

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User with id: %d is not found", id));
        }

        log.debug("User with ID={} is deleted from repository.", id);
    }
}
