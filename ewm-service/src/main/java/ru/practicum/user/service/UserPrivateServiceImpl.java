package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.user.dto.UserResponseIdAndName;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPrivateServiceImpl implements UserPrivateService {

    private static final Sort SORT_BY_RATING = Sort.by(Sort.Direction.DESC, "rate");
    private final UserRepository repository;
    private final UserMapper mapper;
    private final MessageSource messageSource;


    @Override
    public List<UserResponseIdAndName> getUsersRating(Long userId, Integer count) {
        log.debug("A rating of users is requested.");
        verifyUserExists(userId);

        Pageable page = PageRequest.of(0, count, SORT_BY_RATING);

        List<UserResponseIdAndName> foundUsers = repository.findAll(page).getContent()
                .stream()
                .filter(user -> user.getRate() != null)
                .map(mapper::toUserResponseIdAndName)
                .collect(Collectors.toList());

        log.debug("A rating of users is received from repository with size of {}.", foundUsers.size());
        return foundUsers;
    }

    private void verifyUserExists(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException(messageSource.getMessage("user.not_found", new Object[]{userId}, null));
        }
    }
}
