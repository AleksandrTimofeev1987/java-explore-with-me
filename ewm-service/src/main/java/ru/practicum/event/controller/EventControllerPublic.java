package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.EndpointHitClient;
import ru.practicum.dto.EndpointHitCreate;
import ru.practicum.event.entity.Event;
import ru.practicum.event.service.EventServicePublic;

@RestController
@RequestMapping(path = "events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPublic {

    private final EventServicePublic service;
    private final EndpointHitClient client;

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable Long eventId) {
        log.debug("Getting event by ID");

        EndpointHitCreate hit = buildEndpointHit();
//        client.createHit()

        return service.getEventById(eventId);
    }

    private EndpointHitCreate buildEndpointHit() {
        EndpointHitCreate hit = new EndpointHitCreate();
        return null;
    }

}
