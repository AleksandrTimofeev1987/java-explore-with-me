package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.dto.EventView;
import ru.practicum.event.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepositoryPrivate extends JpaRepository<Event, Long> {

    List<EventView> findEventByInitiatorId(Long userId, Pageable page);

    Optional<Event> findEventByInitiatorIdAndId(Long userId, Long eventId);
}