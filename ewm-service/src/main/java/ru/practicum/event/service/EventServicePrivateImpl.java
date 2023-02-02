package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.entity.Category;
import ru.practicum.category.repository.CategoryRepositoryPublic;
import ru.practicum.event.dto.EventCreate;
import ru.practicum.event.dto.EventUpdatePrivate;
import ru.practicum.event.dto.EventView;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.Location;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.repository.EventRepositoryPrivate;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.ForbiddenException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.user.entity.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImpl implements EventServicePrivate {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final EventRepositoryPrivate eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepositoryPublic categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventView> getEvents(Long userId, Integer from, Integer size) {
        log.debug("A list of events initiated by user with id={} is requested with the following pagination parameters: from={} and size={}.", userId, from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
        List<EventView> foundEvents = eventRepository.findEventByInitiatorId(userId, page);

        log.debug("A list of events is received from repository with size of {}.", foundEvents.size());
        return foundEvents;
    }

    @Override
    public Event getEventById(Long userId, Long eventId) {
        log.debug("Event with id={} is requested by user with id={}.", eventId, userId);
        Event foundEvent = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(() -> new NotFoundException(String.format("Event with id=%d is not found", eventId)));
        log.debug("Event with id={} is received from repository.", eventId);
        return foundEvent;
    }

    @Override
    public Event createEvent(Long userId, EventCreate eventDto) {
        log.debug("Request to add event with title={} is received.", eventDto.getTitle());

        validateEventDate(eventDto.getEventDate());

        Event event = buildNewEvent(userId, eventDto);


        Event createdEvent;
        try {
            createdEvent = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Event title is duplicate.");
        }

        log.debug("Event with ID={} is added to repository.", createdEvent.getId());
        return createdEvent;
    }

    private Event buildNewEvent(Long userId, EventCreate eventDto) {
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException(String.format("Category with id=%d is not found", eventDto.getCategory())));
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id=%d is not found", userId)));
        Location location = locationRepository.save(eventDto.getLocation());

        Event event = eventMapper.toEventEntity(userId, eventDto);

        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(location);
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

        return event;
    }

    @Override
    public Event updateEvent(Long userId, Long eventId, EventUpdatePrivate eventDto) {
        log.debug("Request to update event with id={} is received from user with id={}.", eventId, userId);
        if (eventDto.getEventDate() != null) {
            validateEventDate(eventDto.getEventDate());
        }

        Event savedEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event with id=%d is not found", eventId)));

        if (!savedEvent.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("User with id=%d have not initiated event with id=%d is not found", userId, eventId));
        }

        if (savedEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }

        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(buildEventForUpdate(eventDto, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Event title is a duplicate.");
        }
        log.debug("Event with ID={} is updated.", eventId);
        return updatedEvent;
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
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException(String.format("Category with id=%d is not found", eventDto.getCategory())));
            event.setCategory(category);
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            Location location = locationRepository.save(eventDto.getLocation());
            event.setLocation(location);
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

        switch (eventDto.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setState(EventState.CANCELED);
                break;

        }
        return event;
    }


    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ForbiddenException("Event date should be at least two hours from now in the future");
        }
    }
}
