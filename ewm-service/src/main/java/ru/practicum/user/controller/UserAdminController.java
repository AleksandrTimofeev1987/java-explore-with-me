package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserCreate;
import ru.practicum.user.dto.UserResponse;
import ru.practicum.user.entity.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.service.UserAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserAdminController {

    private final UserAdminService service;
    private final UserMapper mapper;

    @GetMapping
    public List<UserResponse> getUsers(@RequestParam(required = false) Long[] ids,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting users");
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserCreate userDto) {
        log.debug("Creating user with name={}", userDto.getName());
        User user = mapper.toUserEntity(userDto);
        return service.createUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable Long id) {
        log.debug("Deleting user with id={}", id);
        service.deleteUser(id);
    }
}
