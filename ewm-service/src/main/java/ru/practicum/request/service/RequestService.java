package ru.practicum.request.service;

import ru.practicum.request.dto.RequestResponse;

import java.util.List;

public interface RequestService {

    /**
     * Method gets request created by user.
     *
     * @param userId ID of user who created requests.
     *
     * @return List of requests.
     */
    List<RequestResponse> getRequests(Long userId);

    /**
     * Method adds request to repository.
     *
     * @param userId ID of user adding request.
     * @param eventId ID of event the request is created for.
     *
     * @return Added event with assigned ID.
     */
    RequestResponse createRequest(Long userId, Long eventId);

    /**
     * Method cancels request for event.
     *
     * @param userId ID of user cancelling request.
     * @param requestId ID of request to be cancelled.
     *
     * @return Cancelled request.
     */
    RequestResponse cancelRequest(Long userId, Long requestId);
}
