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
import ru.practicum.exception.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final CompilationRepository repository;
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

        List<Compilation> foundCompilations = repository.findAll(expression, page).getContent();

        log.debug("A list of compilations is received from repository with size of {}.", foundCompilations.size());
        return foundCompilations
                .stream()
                .map(mapper::toCompilationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponse getCompilationById(Long compId) {
        log.debug("Compilation with compId={} is requested.", compId);
        Compilation foundCompilation = repository.findById(compId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("compilation.not_found", new Object[] {compId}, null)));
        log.debug("Compilation with id={} is received from repository.", compId);
        return mapper.toCompilationResponse(foundCompilation);
    }
}
