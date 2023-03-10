package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventResponseFull;
import ru.practicum.event.dto.EventUpdateAdmin;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.service.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAdminController {

    private final EventAdminService service;

    @GetMapping
    public List<EventResponseFull> getEvents(@RequestParam(required = false) Long[] users,
                                             @RequestParam(required = false) EventState[] states,
                                             @RequestParam(required = false) Long[] categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting events");
        return service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventResponseFull updateEvent(@PathVariable Long eventId,
                             @Valid @RequestBody EventUpdateAdmin eventDto) {
        log.debug("Updating event with id={}", eventId);
        return service.updateEvent(eventId, eventDto);
    }

}
