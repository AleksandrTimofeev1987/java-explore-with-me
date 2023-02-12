package ru.practicum.event.service;

import ru.practicum.event.controller.SearchSort;
import ru.practicum.event.dto.EventResponseFull;
import ru.practicum.event.dto.EventResponseShort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    /**
     * Method gets all events falling under specified filters.
     *
     * @param text          IDs of users who initiated events.
     * @param categories    IDs of categories of the events.
     * @param paid          Should the events be only paid.
     * @param rangeStart    Start of sample period.
     * @param rangeEnd      End of sample period.
     * @param onlyAvailable Should the events be only available (have empty slots).
     * @param sort          How to sort elements.
     * @param from          Index of first element in the sample.
     * @param size          Size of elements shown on one page.
     *
     * @return List of events.
     */
    List<EventResponseShort> getEvents(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SearchSort sort, Integer from, Integer size);

    /**
     * Method gets event by id from repository.
     *
     * @param eventId ID if event that the user requested.
     *
     * @return Event with correct ID.
     */
    EventResponseFull getEventById(Long eventId);
}
