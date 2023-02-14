package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestResponse;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestService service;

    @GetMapping
    public List<RequestResponse> getRequests(@PathVariable Long userId) {
        log.debug("Getting requests created by user with id={}", userId);
        return service.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createRequest(@PathVariable Long userId,
                                         @RequestParam Long eventId) {
        log.debug("Creating event participation request for event with id={} by user with id={}", eventId, userId);
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestResponse cancelRequest(@PathVariable Long userId,
                                         @PathVariable Long requestId) {
        log.debug("Cancelling event participation request for request with id={} by user with id={}", requestId, userId);
        return service.cancelRequest(userId, requestId);
    }
}
