package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;

import java.util.Optional;

@Repository
public interface EventRepositoryPublic extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Optional<Event> findEventByIdAndState(Long eventId, EventState published);
}
