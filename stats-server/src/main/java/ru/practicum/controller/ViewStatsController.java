package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.BadRequestException;
import ru.practicum.service.ViewStatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ViewStatsController {

    private final ViewStatsService service;

    @GetMapping
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.debug("Request to get statistics is received with parameters start={}, end={}", start, end);
        validateParams(start, end);
        return service.getStats(start, end, uris, unique);
    }

    private void validateParams(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Start of requested period should be before end");
        }
    }
}
