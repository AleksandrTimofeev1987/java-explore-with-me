package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.entity.Category;
import ru.practicum.category.repository.CategoryRepositoryPublic;
import ru.practicum.event.dto.EventUpdateAdmin;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.Location;
import ru.practicum.event.entity.QEvent;
import ru.practicum.event.repository.EventRepositoryAdmin;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final EventRepositoryAdmin eventRepository;
    private final CategoryRepositoryPublic categoryRepository;
    private final LocationRepository locationRepository;


    @Override
    public List<Event> getEvents(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.debug("A list of events is requested with the following pagination parameters: from={} and size={}.", from, size);
        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);

        QEvent qEvent = QEvent.event;

        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        if (users != null) {
            expression = expression.and(qEvent.initiator.id.in(users));
        }
        if (states != null) {
            expression = expression.and(qEvent.state.in(states));
        }
        if (categories != null) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.before(rangeEnd));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.before(rangeEnd));
        }

        List<Event> foundEvents = eventRepository.findAll(expression, page).getContent();

        log.debug("A list of events is received from repository with size of {}.", foundEvents.size());
        return foundEvents;
    }

    @Override
    public Event updateEvent(Long eventId, EventUpdateAdmin eventDto) {
        log.debug("Request to update event with id={} is received from admin.", eventId);

        if (eventDto.getEventDate() != null) {
            validateEventDate(eventDto.getEventDate());
        }

        Event savedEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event with id=%d is not found", eventId)));

        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(formEventForUpdate(eventDto, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Event title is a duplicate.");
        }
        log.debug("Event with ID={} is updated.", eventId);
        return updatedEvent;
    }

    private Event formEventForUpdate(EventUpdateAdmin eventDto, Event event) {
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
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new ConflictException("Event can be published only if its' current state is PENDING");
                }
                break;
            case REJECT_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.CANCELED);
                } else {
                    throw new ConflictException("Event can be rejected only if its' current state is PENDING");
                }
                break;

        }
        return event;
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ConflictException("Event date should be at least two hours from now in the future");
        }
    }
}
