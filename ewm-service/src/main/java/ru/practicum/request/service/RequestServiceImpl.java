package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.ForbiddenException;
import ru.practicum.request.dto.RequestResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.request.entity.Request;
import ru.practicum.request.entity.RequestStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.user.entity.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final MessageSource messageSource;

    @Override
    public List<RequestResponse> getRequests(Long userId) {
        log.debug("A list of requests created by user with id={} is requested", userId);

        List<Request> foundRequests = requestRepository.findRequestByRequesterId(userId);

        log.debug("A list of requests is received from repository with size of {}.", foundRequests.size());
        return foundRequests
                .stream()
                .map(requestMapper::toRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RequestResponse createRequest(Long userId, Long eventId) {
        log.debug("Request to add request for event with id={} is received from user with id={}.", eventId, userId);
        verifyUserExists(userId);
        verifyEventExists(eventId);
        verifyNoPriorRequests(userId, eventId);
        verifyNotInitiator(userId, eventId);
        verifyEventPublishedAndHasSlots(eventId);

        Request request = buildNewRequest(userId, eventId);

        Request createdRequest = requestRepository.save(request);

        log.debug("Request with id={} is added to repository.", createdRequest.getId());
        return requestMapper.toRequestResponse(createdRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RequestResponse cancelRequest(Long userId, Long requestId) {
        log.debug("Request to cancel request with id={} is received from user with id={}.", requestId, userId);
        verifyUserExists(userId);

        Request requestToCancel = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("request.not_found", new Object[] {requestId}, null)));

        if (requestToCancel.getStatus().equals(RequestStatus.CANCELED)) {
            throw new ForbiddenException(messageSource.getMessage("request.already_canceled", new Object[] {requestId}, null));
        }

        verifyUserCreatedRequest(userId, requestToCancel);

        requestToCancel.setStatus(RequestStatus.CANCELED);

        Request cancelledRequest = requestRepository.save(requestToCancel);
        Event event = eventRepository.findById(cancelledRequest.getEvent().getId()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[] {cancelledRequest.getEvent().getId()}, null)));
        if (event.getConfirmedRequests() > 0) {
            eventRepository.setEventConfirmedRequests(cancelledRequest.getEvent().getId(), event.getConfirmedRequests() - 1);
        }

        log.debug("Request with id={} is cancelled. Status={}", cancelledRequest.getId(), cancelledRequest.getStatus());
        return requestMapper.toRequestResponse(cancelledRequest);
    }

    private Request buildNewRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("user.not_found", new Object[] {userId}, null)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[] {eventId}, null)));

        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.setEventConfirmedRequests(eventId, event.getConfirmedRequests() + 1);
        }

        return request;
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(messageSource.getMessage("user.not_found", new Object[] {userId}, null));
        }
    }

    private void verifyEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(messageSource.getMessage("event.not_found", new Object[] {eventId}, null));
        }
    }

    private void verifyNoPriorRequests(Long userId, Long eventId) {
        if (requestRepository.findRequestByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(messageSource.getMessage("request.repeated", null, null));
        }
    }

    private void verifyNotInitiator(Long userId, Long eventId) {
        if (eventRepository.findEventByInitiatorIdAndId(userId, eventId).isPresent()) {
            throw new ConflictException(messageSource.getMessage("request.requester_is_initiator", null, null));
        }
    }

    private void verifyEventPublishedAndHasSlots(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(messageSource.getMessage("event.request.unpublished", null, null));
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            throw new ConflictException(messageSource.getMessage("event.request.limit_reached", null, null));
        }
    }

    private void verifyUserCreatedRequest(Long userId, Request request) {
        if (!request.getRequester().getId().equals(userId)) {
            throw new ForbiddenException(messageSource.getMessage("user.request.not_requester", new Object[] {request.getId(), userId}, null));
        }
    }
}
