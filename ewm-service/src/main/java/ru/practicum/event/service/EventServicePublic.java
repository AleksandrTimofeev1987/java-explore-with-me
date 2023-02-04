package ru.practicum.event.service;

import ru.practicum.event.entity.Event;

public interface EventServicePublic {

    /**
     * Method gets event by id from repository.
     *
     * @param eventId ID if event that the user requested.
     *
     * @return Event with correct ID.
     */
    Event getEventById(Long eventId);
}
