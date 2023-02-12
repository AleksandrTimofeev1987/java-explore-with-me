package ru.practicum.compilation.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.compilation.entity.QCompilation;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventIdAvRate;
import ru.practicum.event.dto.EventResponseShort;
import ru.practicum.event.repository.RateEventRepository;
import ru.practicum.exception.model.NotFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final CompilationRepository compilationRepository;
    private final RateEventRepository rateRepository;
    private final CompilationMapper mapper;
    private final MessageSource messageSource;

    @Override
    public List<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("A list of pinned={} compilations is requested with the following pagination parameters: from={} and size={}.", pinned, from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);

        QCompilation qCompilation = QCompilation.compilation;

        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        if (pinned != null) {
            expression = expression.and(qCompilation.pinned.eq(pinned));
        }

        List<Compilation> foundCompilations = compilationRepository.findAll(expression, page).getContent();
        List<CompilationResponse> compilationsReturn = foundCompilations
                .stream()
                .map(mapper::toCompilationResponse)
                .collect(Collectors.toList());

        populateCompilationsEventsWithRates(compilationsReturn);

        log.debug("A list of compilations is received from repository with size of {}.", foundCompilations.size());
        return compilationsReturn;
    }

    @Override
    public CompilationResponse getCompilationById(Long compId) {
        log.debug("Compilation with compId={} is requested.", compId);

        Compilation foundCompilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("compilation.not_found", new Object[] {compId}, null)));
        CompilationResponse foundCompilationDto = mapper.toCompilationResponse(foundCompilation);
        foundCompilationDto.setEvents(buildEventResponses(foundCompilationDto.getEvents()));

        log.debug("Compilation with id={} is received from repository.", compId);
        return foundCompilationDto;
    }

    private void populateCompilationsEventsWithRates(List<CompilationResponse> compilationsReturn) {
        compilationsReturn.forEach(compilation -> compilation.setEvents(buildEventResponses(compilation.getEvents())));
    }

    private List<EventResponseShort> buildEventResponses(List<EventResponseShort> events) {
        Map<Long, EventResponseShort> eventMap = events
                .stream()
                .collect(Collectors.toMap(EventResponseShort::getId, Function.identity()));

        Map<Long, EventIdAvRate> eventsRates = rateRepository.getAverageRatesByEvents(eventMap.keySet())
                .stream()
                .collect(Collectors.toMap(EventIdAvRate::getEventId, Function.identity()));

        if (!eventsRates.isEmpty()) {
            eventMap.values().forEach(event -> event.setRate(eventsRates.get(event.getId()).getRate()));
        }

        return new ArrayList<>(eventMap.values());
    }
}
