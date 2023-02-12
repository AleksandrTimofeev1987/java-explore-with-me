package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.dto.EventIdAvRate;
import ru.practicum.event.entity.RateEvent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RateEventRepository extends JpaRepository<RateEvent, Long> {

    @Query(value = "" +
            "SELECT r " +
            "FROM RateEvent r " +
            "WHERE r.user.id = :userId AND " +
            "r.event.id = :eventId")
    Optional<RateEvent> findRateEventByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    List<RateEvent> findRateEventByUserId(Long userId);

    @Query(value = "" +
            "SELECT new ru.practicum.event.dto.EventIdAvRate(r.event.id, AVG(r.rate)) " +
            "FROM RateEvent r " +
            "WHERE r.event.id IN :events " +
            "GROUP BY r.event.id")
    List<EventIdAvRate> getAverageRatesByEvents(@Param("events") Set<Long> events);

    @Query(value = "" +
            "SELECT AVG(r.rate) " +
            "FROM RateEvent r " +
            "WHERE r.event.id = :eventId")
    Double calculateEventAverageRateById(@Param("eventId") Long eventId);
}
