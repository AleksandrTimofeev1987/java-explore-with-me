package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationCreate;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.dto.CompilationUpdate;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationIAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;
    private final MessageSource messageSource;

    @Override
    public CompilationResponse createCompilation(CompilationCreate compilationDto) {
        log.debug("Request to add compilation with title={} is received.", compilationDto.getTitle());

        Compilation compilation = buildNewCompilation(compilationDto);

        Compilation createdCompilation;
        try {
            createdCompilation = compRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("title.compilation.duplicate", null, null));
        }

        log.debug("Compilation with ID={} is added to repository.", createdCompilation.getId());
        return mapper.toCompilationResponse(createdCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.debug("Request to delete compilation with id={} is received.", compId);

        try {
            compRepository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(messageSource.getMessage("compilation.not_found", new Object[] {compId}, null));
        }

        log.debug("Compilation with id={} is deleted from repository.", compId);
    }

    @Override
    public CompilationResponse updateCompilation(Long compId, CompilationUpdate compilationDto) {
        log.debug("Request to update compilation with id={} is received.", compId);

        Compilation savedCompilation = compRepository.findById(compId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("compilation.not_found", new Object[] {compId}, null)));

        Compilation compilationForUpdate = buildCompilationForUpdate(compilationDto, savedCompilation);

        Compilation updatedCompilation;
        try {
            updatedCompilation = compRepository.save(compilationForUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("title.compilation.duplicate", null, null));
        }

        log.debug("Compilation with id={} is updated in repository.", compId);
        return mapper.toCompilationResponse(updatedCompilation);
    }

    private Compilation buildNewCompilation(CompilationCreate compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents().length != 0) {
            events = eventRepository.findEventsByIds(compilationDto.getEvents());
        }
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.getPinned());

        return compilation;
    }

    private Compilation buildCompilationForUpdate(CompilationUpdate compilationDto, Compilation savedCompilation) {
        if (compilationDto.getTitle() != null) {
            savedCompilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getEvents() != null) {
            List<Event> events = new ArrayList<>();
            if (compilationDto.getEvents().length != 0) {
                events = eventRepository.findEventsByIds(compilationDto.getEvents());
            }
            savedCompilation.setEvents(events);
        }
        if (compilationDto.getPinned() != null) {
            savedCompilation.setPinned(compilationDto.getPinned());
        }
        return savedCompilation;
    }
}
