package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationCreate;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.dto.CompilationUpdate;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse createCompilation(@Valid @RequestBody CompilationCreate compilationDto) {
        log.debug("Creating compilation with title={}", compilationDto.getTitle());
        return service.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable Long compId) {
        log.debug("Deleting compilation with id={}", compId);
        service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationResponse updateCompilation(@Positive @PathVariable Long compId,
                                                 @RequestBody CompilationUpdate compilationDto) {
        log.debug("Updating compilation with id={}", compId);
        return service.updateCompilation(compId, compilationDto);
    }
}
