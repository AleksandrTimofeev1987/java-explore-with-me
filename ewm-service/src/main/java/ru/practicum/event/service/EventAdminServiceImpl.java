package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.entity.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventUpdateAdmin;
import ru.practicum.event.dto.StateActionAdmin;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;
    Map<StateActionAdmin, Consumer<Event>> settingEventStatusMap = Map.of(
            StateActionAdmin.PUBLISH_EVENT, this::setEventStatusPublished,
            StateActionAdmin.REJECT_EVENT, this::setEventStatusCanceled
    );

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

        Event savedEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[] {eventId}, null)));

        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(buildEventForUpdate(eventDto, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("title.event.duplicate", null, null));
        }
        log.debug("Event with ID={} is updated.", eventId);
        return updatedEvent;
    }

    private Event buildEventForUpdate(EventUpdateAdmin eventDto, Event event) {
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
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not_found", new Object[] {eventDto.getCategory()}, null)));
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

    private void setEventStatusPublished(Event event) {
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            throw new ConflictException(messageSource.getMessage("state.event.publish.not_pending", null, null));
        }
    }

    private void setEventStatusCanceled(Event event) {
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
        } else {
            throw new ConflictException(messageSource.getMessage("state.event.cancel.not_pending", null, null));
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ConflictException(messageSource.getMessage("date.event.not_valid", null, null));
        }
    }
}
