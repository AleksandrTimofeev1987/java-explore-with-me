package ru.practicum.event.service;

import ru.practicum.event.dto.EventUpdateAdmin;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServiceAdmin {

    /**
     * Method gets all events falling under specified filters.
     *
     * @param users         IDs of users who initiated events.
     * @param states        States of events to be picked.
     * @param categories    IDs of categories of the events.
     * @param rangeStart    Start of sample period.
     * @param rangeEnd      End of sample period.
     * @param from          Index of first element in the sample.
     * @param size          Size of elements shown on one page.
     *
     * @return List of events.
     */
    List<Event> getEvents(Long[] users,
                          EventState[] states,
                          Long[] categories,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          Integer from,
                          Integer size);

    /**
     * Method updates event in repository.
     *
     * @param eventId ID if event that the user requested to update.
     * @param eventDto Category with updated fields.
     *
     * @return Updated event.
     */
    Event updateEvent(Long eventId, EventUpdateAdmin eventDto);
}
