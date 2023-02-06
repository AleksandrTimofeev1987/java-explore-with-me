package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.EndpointHitClient;
import ru.practicum.dto.EndpointHitCreate;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPublic {

    private final EventServicePublic service;
    private final EndpointHitClient client;

    @GetMapping
    public List<EventResponse> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) Long[] categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false, defaultValue = "EVENT_DATE") SearchSort sort,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.debug("Getting events");

        EndpointHitCreate hit = buildEndpointHit(request);
        client.createHit(hit);

        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.debug("Getting event by ID");

        EndpointHitCreate hit = buildEndpointHit(request);
        client.createHit(hit);

        return service.getEventById(eventId);
    }

    private EndpointHitCreate buildEndpointHit(HttpServletRequest request) {
        EndpointHitCreate hit = new EndpointHitCreate();
        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI());
        hit.setIp(request.getRequestURI());

        hit.setTimestamp(LocalDateTime.now());
        return hit;
    }

}
