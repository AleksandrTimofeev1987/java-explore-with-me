package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.RateEventCreate;
import ru.practicum.event.dto.RateEventResponse;
import ru.practicum.event.service.RateEventPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}/events/rate")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RateEventPrivateController {

    private final RateEventPrivateService service;

    @GetMapping
    public List<RateEventResponse> getRatesByCreator(@PathVariable Long userId) {
        log.debug("List of events rates created by user with id={} is requested.", userId);
        return service.getRatesByCreator(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RateEventResponse rateEvent(@PathVariable Long userId, @Valid @RequestBody RateEventCreate rateEventDto) {
        log.debug("Rating event with id={}", rateEventDto.getEvent());
        rateEventDto.setUser(userId);
        return service.rateEvent(rateEventDto);
    }

    @DeleteMapping("/{rateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long userId, @Positive @PathVariable Long rateId) {
        log.debug("Deleting rate with id={} of event ", rateId);
        service.deleteRateEvent(userId, rateId);
    }

    @PatchMapping("/{rateId}")
    public RateEventResponse updateRate(@PathVariable Long userId,
                                      @PathVariable Long rateId,
                                      @RequestParam Integer rate) {
        log.debug("Updating event rate for rate with id={} by user with id={}", rateId, userId);
        return service.updateRate(userId, rateId, rate);
    }
}
