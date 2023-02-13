package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.RateEventCreate;
import ru.practicum.event.dto.RateEventResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.RateEvent;
import ru.practicum.event.mapper.RateEventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.RateEventRepository;
import ru.practicum.exception.model.ForbiddenException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.request.entity.Request;
import ru.practicum.request.entity.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.entity.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateEventPrivateServiceImpl implements RateEventPrivateService {

    private final RateEventRepository rateRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final MessageSource messageSource;
    private final RateEventMapper mapper;

    @Override
    public List<RateEventResponse> getRatesByCreator(Long userId) {
        log.debug("A list of events rates created by user with id={} is requested", userId);

        verifyUserExists(userId);

        List<RateEvent> foundRateEvents = rateRepository.findRateEventByUserId(userId);

        log.debug("A list of events rates  is received from repository with size of {}.", foundRateEvents.size());
        return foundRateEvents
                .stream()
                .map(mapper::toRateEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RateEventResponse rateEvent(RateEventCreate rateEventDto) {
        log.debug("Request to rate event with id={} is received.", rateEventDto.getEvent());

        User user = userRepository.findById(rateEventDto.getUser()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("user.not_found", new Object[]{rateEventDto.getUser()}, null)));
        Event event = eventRepository.findById(rateEventDto.getEvent()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[]{rateEventDto.getEvent()}, null)));

        validateUserNotInitiator(rateEventDto.getUser(), event.getInitiator().getId());
        validateUserHasConfirmedRequestForThisEvent(rateEventDto.getUser(), event.getId());
        validateUserHasNotRatedThisEvent(user.getId(), event.getId());

        RateEvent rateEvent = buildNewRate(user, event, rateEventDto.getRate());

        RateEvent createdRateEvent = rateRepository.save(rateEvent);

        log.debug("Event with id={} was rated. Rate event id={}, rate={}.", createdRateEvent.getEvent().getId(), createdRateEvent.getId(), createdRateEvent.getRate());
        return mapper.toRateEventResponse(createdRateEvent);
    }

    @Override
    public void deleteRateEvent(Long userId, Long rateId) {
        log.debug("Request to delete event rate with id={} is received.", rateId);

        verifyUserExists(userId);
        validateRateExistsAndUserCreatedRate(userId, rateId);

        rateRepository.deleteById(rateId);

        log.debug("Rate with id={} was deleted.", rateId);
    }

    @Override
    public RateEventResponse updateRate(Long userId, Long rateId, Integer rate) {
        log.debug("Request to update event rate with id={} is received.", rateId);

        verifyUserExists(userId);
        RateEvent savedRate = rateRepository.findById(rateId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("rate.not_found", new Object[]{rateId}, null)));
        validateUserCreatedRate(userId, savedRate);

        savedRate.setRate(rate);
        RateEvent updatedRate = rateRepository.save(savedRate);

        log.debug("Rate with id={} was updated.", rateId);
        return mapper.toRateEventResponse(updatedRate);
    }

    private RateEvent buildNewRate(User user, Event event, Integer rate) {
        RateEvent rateEvent = new RateEvent();
        rateEvent.setUser(user);
        rateEvent.setEvent(event);
        rateEvent.setRate(rate);

        return rateEvent;
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(messageSource.getMessage("user.not_found", new Object[]{userId}, null));
        }
    }

    private void validateUserNotInitiator(Long userId, Long initiatorId) {
        if (userId.equals(initiatorId)) {
            throw new ForbiddenException(messageSource.getMessage("user.rate.initiator", null, null));
        }
    }

    private void validateUserHasConfirmedRequestForThisEvent(Long userId, Long eventId) {
        Request request = requestRepository.findRequestByRequesterIdAndEventId(userId, eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("request.rate.not_found", null, null)));
        if (!request.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new ForbiddenException(messageSource.getMessage("request.rate.not_confirmed", null, null));
        }
    }

    private void validateUserHasNotRatedThisEvent(Long userId, Long eventId) {
        if (rateRepository.findRateEventByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException(messageSource.getMessage("rate.already_exists", null, null));
        }
    }

    private void validateRateExistsAndUserCreatedRate(Long userId, Long rateId) {
        RateEvent rateEvent = rateRepository.findById(rateId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("rate.not_found", new Object[]{rateId}, null)));

        if (!rateEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException(messageSource.getMessage("user.rate.not_creator", null, null));
        }
    }

    private void validateUserCreatedRate(Long userId, RateEvent rateEvent) {
        if (!rateEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException(messageSource.getMessage("user.rate.not_creator", null, null));
        }
    }
}
