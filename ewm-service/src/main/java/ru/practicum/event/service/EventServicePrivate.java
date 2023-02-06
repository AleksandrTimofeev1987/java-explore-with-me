package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.entity.Event;
import ru.practicum.request.dto.RequestResponse;

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
    List<EventResponse> getEvents(Long userId, Integer from, Integer size);

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

    /**
     * Method gets all requests created for user's event.
     *
     * @param userId ID of user requesting events.
     * @param eventId ID of event initiated by user.
     *
     * @return List of requests created for user's event.
     */
    List<RequestResponse> getRequests(Long userId, Long eventId);

    /**
     * Method updates requests statuses for particular user's event.
     *
     * @param userId ID of user requesting events.
     * @param eventId ID of event initiated by user.
     * @param updateDto Object containing list of request ids and status to be applied
     *
     * @return Object containing lists of confirmed and rejected requests.
     */
    RequestStatusUpdateResponse updateRequests(Long userId, Long eventId, RequestStatusUpdateRequest updateDto);
}
