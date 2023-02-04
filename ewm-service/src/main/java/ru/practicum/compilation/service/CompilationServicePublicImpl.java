package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepositoryPublic;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServicePublicImpl implements CompilationServicePublic {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final CompilationRepositoryPublic repository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("A list of pinned={} compilations is requested with the following pagination parameters: from={} and size={}.", pinned, from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
        List<Compilation> foundCompilations;
        if (pinned == null) {
            foundCompilations = repository.findAll(page).getContent();
        } else {
            foundCompilations = repository.findCompilationByPinned(pinned, page);
        }

        log.debug("A list of compilations is received from repository with size of {}.", foundCompilations.size());
        return foundCompilations
                .stream()
                .map(mapper::toCompilationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponse getCompilationById(Long compId) {
        log.debug("Compilation with compId={} is requested.", compId);
        Compilation foundCompilation = repository.findById(compId).orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d is not found", compId)));
        log.debug("Compilation with id={} is received from repository.", compId);
        return mapper.toCompilationResponse(foundCompilation);
    }
}
