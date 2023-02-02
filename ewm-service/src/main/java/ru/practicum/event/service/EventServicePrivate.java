package ru.practicum.event.service;

import ru.practicum.event.dto.EventCreate;
import ru.practicum.event.dto.EventUpdatePrivate;
import ru.practicum.event.dto.EventView;
import ru.practicum.event.entity.Event;

import java.util.List;

public interface EventServicePrivate {

    /**
     * Method gets all events initiated by user from repository.
     *
     * @param userId ID of user requesting events.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of events initiated by user.
     */
    List<EventView> getEvents(Long userId, Integer from, Integer size);

    /**
     * Method gets event initiated by user from repository.
     *
     * @param userId ID of user requesting events.
     * @param eventId ID if event that the user requested.
     *
     * @return Event initiated by user with correct ID.
     */
    Event getEventById(Long userId, Long eventId);

    /**
     * Method adds event to repository.
     *
     * @param userId ID of user adding event.
     * @param eventDto Event to be added.
     *
     * @return Added event with assigned ID.
     */
    Event createEvent(Long userId, EventCreate eventDto);

    /**
     * Method updates event in repository.
     *
     * @param userId ID of user updating event.
     * @param eventId ID if event that the user requested to update.
     * @param eventDto Event with updated fields.
     *
     * @return Updated event.
     */
    Event updateEvent(Long userId, Long eventId, EventUpdatePrivate eventDto);
}
