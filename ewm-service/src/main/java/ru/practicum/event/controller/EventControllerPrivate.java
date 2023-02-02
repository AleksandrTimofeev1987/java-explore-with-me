package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventCreate;
import ru.practicum.event.dto.EventUpdate;
import ru.practicum.event.dto.EventView;
import ru.practicum.event.entity.Event;
import ru.practicum.event.service.EventServicePrivate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPrivate {

    private final EventServicePrivate service;

    @GetMapping
    public List<EventView> getEvents(@PathVariable Long userId,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting events");
        return service.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable Long userId,
                              @PathVariable Long eventId) {
        log.debug("Getting event by ID");
        return service.getEventById(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@PathVariable Long userId, @Valid @RequestBody EventCreate eventDto) {
        log.debug("Creating event with title={}", eventDto.getTitle());
        return service.createEvent(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public Event createEvent(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @Valid @RequestBody EventUpdate eventDto) {
        log.debug("Updating event with id={}", eventId);
        return service.updateEvent(userId, eventId, eventDto);
    }

}
