package ru.practicum.event.service;


import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.entity.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.QEvent;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.request.dto.RequestResponse;
import ru.practicum.request.entity.Request;
import ru.practicum.request.entity.RequestStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.entity.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private static final Sort SORT_BY_RATING = Sort.by(Sort.Direction.DESC, "rate");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final MessageSource messageSource;
    private final Map<StateActionPrivate, Consumer<Event>> settingEventStatusMap = Map.of(
            StateActionPrivate.SEND_TO_REVIEW, event -> event.setState(EventState.PENDING),
            StateActionPrivate.CANCEL_REVIEW, event -> event.setState(EventState.CANCELED)
    );

    @Override
    public List<EventResponseShort> getEvents(Long userId, Integer from, Integer size) {
        log.debug("A list of events initiated by user with id={} is requested with the following pagination parameters: from={} and size={}.", userId, from, size);
        verifyUserExists(userId);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
        List<Event> foundEvents = eventRepository.findEventsByInitiatorId(userId, page);

        log.debug("A list of events is received from repository with size of {}.", foundEvents.size());
        return foundEvents
                .stream()
                .map(eventMapper::toEventResponseShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponseShort> getMostRatedEvents(Long userId, Integer count, Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        log.debug("A list of most rated events is requested.");
        verifyUserExists(userId);

        Pageable page = PageRequest.of(0, count, SORT_BY_RATING);

        BooleanExpression expression = buildExpressionForRating(categories, paid, rangeStart, rangeEnd);

        List<EventResponseShort> foundEvents = eventRepository.findAll(expression, page).getContent()
                .stream()
                .map(eventMapper::toEventResponseShort)
                .collect(Collectors.toList());

        log.debug("A list of most rated events is received from repository with size of {}.", foundEvents.size());
        return foundEvents;
    }

    @Override
    public EventResponseFull getEventById(Long userId, Long eventId) {
        log.debug("Event with id={} is requested by user with id={}.", eventId, userId);
        verifyUserExists(userId);

        Event foundEvent = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[]{eventId}, null)));
        log.debug("Event with id={} is received from repository.", eventId);
        return eventMapper.toEventResponseFull(foundEvent);
    }

    @Override
    public EventResponseFull createEvent(Long userId, EventCreate eventDto) {
        log.debug("Request to add event with title={} is received.", eventDto.getTitle());
        verifyUserExists(userId);

        validateEventDate(eventDto.getEventDate());

        Event event = buildNewEvent(userId, eventDto);

        Event createdEvent;
        try {
            createdEvent = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("title.event.duplicate", null, null));
        }

        log.debug("Event with ID={} is added to repository.", createdEvent.getId());
        return eventMapper.toEventResponseFull(createdEvent);
    }

    @Override
    public EventResponseFull updateEvent(Long userId, Long eventId, EventUpdatePrivate eventDto) {
        log.debug("Request to update event with id={} is received from user with id={}.", eventId, userId);
        verifyUserExists(userId);

        if (eventDto.getEventDate() != null) {
            validateEventDate(eventDto.getEventDate());
        }

        Event savedEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[]{eventId}, null)));

        if (!savedEvent.getInitiator().getId().equals(userId)) {
            throw new ConflictException(messageSource.getMessage("event.not_initiator", new Object[]{userId, eventId}, null));
        }

        if (savedEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(messageSource.getMessage("state.event.change.not_pending_or_cancelled", null, null));
        }

        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(buildEventForUpdate(eventDto, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("title.event.duplicate", null, null));
        }
        log.debug("Event with ID={} is updated.", eventId);
        return eventMapper.toEventResponseFull(updatedEvent);
    }

    @Override
    public List<RequestResponse> getRequests(Long userId, Long eventId) {
        log.debug("A list of requests created for event with id={} initiated by user with id={} is requested", eventId, userId);
        verifyUserExists(userId);
        verifyEventExists(eventId);

        List<Request> foundRequests = requestRepository.findRequestByEventId(userId, eventId);

        log.debug("A list of requests is received from repository with size of {}.", foundRequests.size());
        return foundRequests
                .stream()
                .map(requestMapper::toRequestResponse)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public RequestStatusUpdateResponse updateRequests(Long userId, Long eventId, RequestStatusUpdateRequest updateDto) {
        log.debug("A request to update requests for event with id={} initiated by user with id={} is received", eventId, userId);
        verifyUserExists(userId);

        Event event = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found_by_initiator", new Object[]{eventId, userId}, null)));

        List<Request> requestsForUpdate = requestRepository.findRequestsForUpdate(eventId, updateDto.getRequestIds());

        RequestStatusUpdateResponse result = updateAndSaveRequests(requestsForUpdate, updateDto.getStatus(), event);

        log.debug("A request status update result object is received");
        return result;
    }

    private Event buildNewEvent(Long userId, EventCreate eventDto) {
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not_found", new Object[]{eventDto.getCategory()}, null)));
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("user.not_found", new Object[]{userId}, null)));

        Event event = eventMapper.toEventEntity(userId, eventDto);

        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(eventDto.getLocation());
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        event.setState(EventState.PENDING);

        event.setRate(0.0);

        return event;
    }

    private RequestStatusUpdateResponse updateAndSaveRequests(List<Request> requestsForUpdate, StateRequestStatus status, Event event) {
        switch (status) {
            case CONFIRMED:
                return confirmRequests(requestsForUpdate, event);
            case REJECTED:
                return rejectRequests(requestsForUpdate);
            default:
                return new RequestStatusUpdateResponse();
        }
    }

    private RequestStatusUpdateResponse confirmRequests(List<Request> requestsForUpdate, Event event) {
        RequestStatusUpdateResponse result = new RequestStatusUpdateResponse();

        for (Request request : requestsForUpdate) {
            try {
                validateRequestStatusPending(request);
            } catch (ConflictException e) {
                continue;
            }

            if (event.getParticipantLimit() - event.getConfirmedRequests() <= 0) {
                throw new ConflictException(messageSource.getMessage("event.confirm.participant_limit", new Object[]{request.getId(), event.getId()}, null));
            } else {
                confirmAndSaveRequest(result, request);
                incrementEventConfirmedRequests(event);
            }
        }

        return result;
    }

    private void confirmAndSaveRequest(RequestStatusUpdateResponse result, Request request) {
        request.setStatus(RequestStatus.CONFIRMED);
        Request confirmedRequest = requestRepository.save(request);
        result.getConfirmedRequests().add(requestMapper.toRequestResponse(confirmedRequest));
    }

    private void incrementEventConfirmedRequests(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.setEventConfirmedRequests(event.getId(), event.getConfirmedRequests());
    }

    private RequestStatusUpdateResponse rejectRequests(List<Request> requestsForUpdate) {
        RequestStatusUpdateResponse result = new RequestStatusUpdateResponse();

        for (Request request : requestsForUpdate) {
            validateRequestStatusPending(request);

            Request rejectedRequest = setRequestRejectedStatus(request);
            result.getRejectedRequests().add(requestMapper.toRequestResponse(rejectedRequest));
        }
        return result;
    }

    private void validateRequestStatusPending(Request request) throws ConflictException {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new ConflictException(messageSource.getMessage("status.request.confirm.not_pending", new Object[]{request.getId()}, null));
        }
    }

    private Request setRequestRejectedStatus(Request request) {
        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    private Event buildEventForUpdate(EventUpdatePrivate eventDto, Event event) {
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not_found", new Object[]{eventDto.getCategory()}, null)));
            event.setCategory(category);
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(event.getLocation());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        settingEventStatusMap.get(eventDto.getStateAction()).accept(event);

        return event;
    }

    private BooleanExpression buildExpressionForRating(Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd) {

        QEvent qEvent = QEvent.event;

        BooleanExpression expression = qEvent.state.eq(EventState.PUBLISHED);

        if (categories != null) {
            expression = expression.and(qEvent.category.id.in(categories));
        }

        if (paid != null) {
            expression = expression.and(qEvent.paid.eq(paid));
        }

        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.before(rangeEnd));
        }

        return expression;
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ConflictException(messageSource.getMessage("date.event.not_valid", null, null));
        }
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(messageSource.getMessage("user.not_found", new Object[]{userId}, null));
        }
    }

    private void verifyEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(messageSource.getMessage("event.not_found", new Object[]{eventId}, null));
        }
    }
}
