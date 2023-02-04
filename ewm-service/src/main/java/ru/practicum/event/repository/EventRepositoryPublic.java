package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.Event;

@Repository
public interface EventRepositoryPublic extends JpaRepository<Event, Long> {
}
