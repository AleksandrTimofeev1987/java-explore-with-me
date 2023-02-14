package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitResponse;
import ru.practicum.entity.EndpointHit;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.EndpointHitRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository repository;
    private final EndpointHitMapper mapper;

    @Override
    public EndpointHitResponse createHit(EndpointHit hit) {
        log.debug("Request to add endpoint hit for app={} is received.", hit.getApp());

        EndpointHit createdHit = repository.save(hit);

        log.debug("Endpoint hit with id={} is added to repository.", createdHit.getId());
        return mapper.toEndpointHitResponse(createdHit);
    }
}
