package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.event.controller.SearchSort;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.QEvent;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {

    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.ASC, "eventDate");
    private static final Sort SORT_BY_VIEWS = Sort.by(Sort.Direction.ASC, "views");
    private final EventRepository repository;
    private final EventMapper mapper;
    private final MessageSource messageSource;

    @Override
    public List<EventResponse> getEvents(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SearchSort sort, Integer from, Integer size) {
        log.debug("A list of events is requested with the following pagination parameters: from={} and size={}.", from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_DATE);

        BooleanExpression expression = buildExpression(text, categories, paid, rangeStart, rangeEnd);

        if (sort.equals(SearchSort.VIEWS)) {
            page = PageRequest.of(from / size, size, SORT_BY_VIEWS);
        }

        List<EventResponse> foundEvents = repository.findAll(expression, page).getContent()
                .stream()
                .filter(event -> event.getParticipantLimit().equals(0) ||
                        (event.getParticipantLimit() - event.getViews()) > 0)
                .map(mapper::toEventResponse)
                .collect(Collectors.toList());

        log.debug("A list of events is received from repository with size of {}.", foundEvents.size());
        return foundEvents;
    }

    @Override
    public Event getEventById(Long eventId) {
        log.debug("Event with id={} is requested.", eventId);

        Event foundEvent = repository.findEventByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(() -> new NotFoundException(messageSource.getMessage("event.not_found", new Object[] {eventId}, null)));

        foundEvent.setViews(foundEvent.getViews() + 1);

        repository.save(foundEvent);

        log.debug("Event with id={} is received from repository.", eventId);
        return foundEvent;
    }

    private BooleanExpression buildExpression(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd) {

        QEvent qEvent = QEvent.event;

        BooleanExpression expression = qEvent.state.eq(EventState.PUBLISHED);

        if (text != null) {
            expression = expression.and(qEvent.annotation.containsIgnoreCase(text).or(qEvent.description.containsIgnoreCase(text)));
        }

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
}
