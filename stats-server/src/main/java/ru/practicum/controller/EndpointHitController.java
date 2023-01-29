package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitCreate;
import ru.practicum.dto.EndpointHitResponse;
import ru.practicum.entity.EndpointHit;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.BadRequestException;
import ru.practicum.service.EndpointHitService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EndpointHitController {

    private final EndpointHitService service;
    private final EndpointHitMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitResponse createHit(@RequestBody @Valid EndpointHitCreate hitDto) {
        log.debug("Creating endpoint hit for app={}", hitDto.getApp());
        validateHit(hitDto);
        EndpointHit hit = mapper.toEndpointHitEntity(hitDto);
        return service.createHit(hit);
    }

    private void validateHit(EndpointHitCreate hitDto) {
        if (hitDto.getApp() == null ||
                hitDto.getApp() == "") {
            throw new BadRequestException("App name should not be Null or Blank");
        }

        if (hitDto.getUri() == null ||
                hitDto.getUri() == "") {
            throw new BadRequestException("Uri should not be Null or Blank");
        }

        if (hitDto.getIp() == null ||
                hitDto.getIp() == "") {
            throw new BadRequestException("Ip should not be Null or Blank");
        }

        if (hitDto.getTimestamp() == null) {
            throw new BadRequestException("Timestamp should not be Null");
        }
    }
}
