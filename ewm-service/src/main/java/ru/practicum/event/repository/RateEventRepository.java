package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.RateEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateEventRepository extends JpaRepository<RateEvent, Long> {

    Optional<RateEvent> findRateEventByUserIdAndEventId(Long userId, Long eventId);

    List<RateEvent> findRateEventByUserId(Long userId);

}
