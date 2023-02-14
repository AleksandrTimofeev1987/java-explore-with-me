package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventPrivateService;
import ru.practicum.request.dto.RequestResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPrivateController {

    private final EventPrivateService service;

    @GetMapping
    public List<EventResponseShort> getEvents(@PathVariable Long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting events");
        return service.getEvents(userId, from, size);
    }

    @GetMapping("/rating")
    public List<EventResponseShort> getMostRatedEvents(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "10") Integer count,
                                                       @RequestParam(required = false) Long[] categories,
                                                       @RequestParam(required = false) Boolean paid,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        log.debug("Getting most rated events with count = {}.", count);

        return service.getMostRatedEvents(userId, count, categories, paid, rangeStart, rangeEnd);
    }

    @GetMapping("/{eventId}")
    public EventResponseFull getEventById(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        log.debug("Getting event by ID");
        return service.getEventById(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseFull createEvent(@PathVariable Long userId, @Valid @RequestBody EventCreate eventDto) {
        log.debug("Creating event with title={}", eventDto.getTitle());
        return service.createEvent(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventResponseFull updateEvent(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @Valid @RequestBody EventUpdatePrivate eventDto) {
        log.debug("Updating event with id={}", eventId);
        return service.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestResponse> getRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("Getting requests for event with id={}", eventId);
        return service.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResponse updateRequests(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      @RequestBody RequestStatusUpdateRequest updateDto) {
        log.debug("Getting requests for event with id={}", eventId);
        return service.updateRequests(userId, eventId, updateDto);
    }
}
