package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserResponseIdAndName;
import ru.practicum.user.service.UserPrivateService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserPrivateController {

    private final UserPrivateService service;

    @GetMapping("/rating")
    public List<UserResponseIdAndName> getUsersRating(@PathVariable Long userId,
                                                      @Positive @RequestParam(defaultValue = "10") Integer count) {
        log.debug("Getting users rating.");
        return service.getUsersRating(userId, count);
    }
}
