package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.request.dto.RequestResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPrivateService {

    /**
     * Method gets all events initiated by user from repository.
     *
     * @param userId ID of user requesting events.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of events initiated by user.
     */
    List<EventResponseShort> getEvents(Long userId, Integer from, Integer size);

    /**
     * Method gets most rated events falling under specified filters.
     *
     * @param userId        ID of user requesting rating.
     * @param count         Number of events in rating.
     * @param categories    IDs of categories of the events.
     * @param paid          Should the events be only paid.
     * @param rangeStart    Start of sample period.
     * @param rangeEnd      End of sample period.
     *
     * @return List of events.
     */
    List<EventResponseShort> getMostRatedEvents(Long userId, Integer count, Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    /**
     * Method gets event initiated by user from repository.
     *
     * @param userId ID of user requesting events.
     * @param eventId ID if event that the user requested.
     *
     * @return Event initiated by user with correct ID.
     */
    EventResponseFull getEventById(Long userId, Long eventId);

    /**
     * Method adds event to repository.
     *
     * @param userId ID of user adding event.
     * @param eventDto Event to be added.
     *
     * @return Added event with assigned ID.
     */
    EventResponseFull createEvent(Long userId, EventCreate eventDto);

    /**
     * Method updates event in repository.
     *
     * @param userId ID of user updating event.
     * @param eventId ID if event that the user requested to update.
     * @param eventDto Event with updated fields.
     *
     * @return Updated event.
     */
    EventResponseFull updateEvent(Long userId, Long eventId, EventUpdatePrivate eventDto);

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
