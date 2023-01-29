package ru.practicum.service;

import ru.practicum.dto.EndpointHitResponse;
import ru.practicum.entity.EndpointHit;

public interface EndpointHitService {
    /**
     * Method creates endpoint hit in repository.
     *
     * @param hitDto Endpoint hit to be created.
     *
     * @return List with ViewStats objects for each URI.
     */
    EndpointHitResponse createHit(EndpointHit hitDto);
}
