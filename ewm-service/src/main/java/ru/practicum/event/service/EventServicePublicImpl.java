package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.entity.Event;
import ru.practicum.event.repository.EventRepositoryPublic;
import ru.practicum.exception.model.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePublicImpl implements EventServicePublic {

    private final EventRepositoryPublic repository;

    @Override
    public Event getEventById(Long eventId) {
        log.debug("Event with id={} is requested.", eventId);

        Event foundEvent = repository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event with id=%d is not found", eventId)));

        foundEvent.setViews(foundEvent.getViews() + 1);

        repository.save(foundEvent);

        log.debug("Event with id={} is received from repository.", eventId);
        return foundEvent;
    }
}
