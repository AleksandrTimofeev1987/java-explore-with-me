package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationResponse;

import java.util.List;

public interface CompilationServicePublic {

    /**
     * Method returns all compilations (if no requirements) or select pinned/unpinned compilations.
     *
     * @param pinned Search for pinned or unpinned compilations.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of compilations.
     */
    List<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Method returns compilation by ID.
     *
     * @param compId ID of compilation to select.
     *
     * @return Compilation with this ID.
     */
    CompilationResponse getCompilationById(Long compId);
}
