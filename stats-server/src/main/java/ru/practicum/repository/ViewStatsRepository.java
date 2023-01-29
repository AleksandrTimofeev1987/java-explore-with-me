package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStats;
import ru.practicum.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewStatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "" +
            "SELECT new ru.practicum.dto.ViewStats(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND " +
            "e.timestamp < :end " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> findByNoUrisAndUniqueFalse(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    @Query(value = "" +
            "SELECT new ru.practicum.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND " +
            "e.timestamp < :end " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> findByNoUrisAndUniqueTrue(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query(value = "" +
            "SELECT new ru.practicum.dto.ViewStats(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND " +
            "e.timestamp < :end AND " +
            "e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> findByUrisAndUniqueFalse(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") String[] uris);

    @Query(value = "" +
            "SELECT new ru.practicum.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start AND " +
            "e.timestamp < :end AND " +
            "e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> findByUrisAndUniqueTrue(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end,
                                            @Param("uris") String[] uris);
}
