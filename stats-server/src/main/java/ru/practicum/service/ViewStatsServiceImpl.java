package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStats;
import ru.practicum.repository.ViewStatsRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewStatsServiceImpl implements ViewStatsService {
    private final ViewStatsRepository repository;

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<ViewStats> stats;
        if (uris == null && !unique) {
            stats = repository.findByNoUrisAndUniqueFalse(start, end);
        } else if (uris == null && unique) {
            stats = repository.findByNoUrisAndUniqueTrue(start, end);
        } else if (uris != null && !unique) {
            stats = repository.findByUrisAndUniqueFalse(start, end, uris);
        } else {
            stats = repository.findByUrisAndUniqueTrue(start, end, uris);
        }

        return stats.stream().sorted(Comparator.comparingLong(ViewStats::getHits).reversed()).collect(Collectors.toList());
    }
}
