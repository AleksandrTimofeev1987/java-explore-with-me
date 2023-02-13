package ru.practicum.user.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
import ru.practicum.user.entity.QUser;
import ru.practicum.user.entity.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final UserRepository repository;
    private final UserMapper mapper;
    private final MessageSource messageSource;


    @Override
    public List<UserResponse> getUsers(Long[] ids, Integer from, Integer size) {
        log.debug("A list of users is requested with the following pagination parameters: from={} and size={}.", from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);

        QUser qUser = QUser.user;

        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        if (ids != null) {
            expression = expression.and(qUser.id.in(ids));
        }

        List<User> foundUsers = repository.findAll(expression, page).getContent();

        log.debug("A list of users is received from repository with size of {}.", foundUsers.size());
        return foundUsers
                .stream()
                .map(mapper::toUserResponseDto)
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
            throw new ConflictException(messageSource.getMessage("email.user.duplicate", null, null));
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
            throw new NotFoundException(messageSource.getMessage("user.not_found", new Object[] {id}, null));
        }

        log.debug("User with ID={} is deleted from repository.", id);
    }
}
