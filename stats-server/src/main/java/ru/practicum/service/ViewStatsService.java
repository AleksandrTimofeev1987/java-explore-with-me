package ru.practicum.service;

import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewStatsService {
    /**
     * Method gets statistics from repository.
     *
     * @param start Start of statistical period.
     * @param end End of statistical period.
     * @param uris Uris for which the statistics is requested.
     * @param unique Should only unique visits be considered.
     *
     * @return Created endpoint hit.
     */
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
