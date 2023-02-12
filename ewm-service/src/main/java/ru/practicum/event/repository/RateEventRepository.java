package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.RateEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateEventRepository extends JpaRepository<RateEvent, Long> {

    @Query(value = "" +
            "SELECT r " +
            "FROM RateEvent r " +
            "WHERE r.user.id = :userId AND " +
            "r.event.id = :eventId")
    Optional<RateEvent> findRateEventByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    List<RateEvent> findRateEventByUserId(Long userId);
}
