package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.service.CompilationServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerPublic {

    private final CompilationServicePublic service;

    @GetMapping
    public List<CompilationResponse> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting compilations");
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponse getCompilationById(@PathVariable @PositiveOrZero Long compId) {
        log.debug("Getting compilation with id={}", compId);
        return service.getCompilationById(compId);
    }
}
