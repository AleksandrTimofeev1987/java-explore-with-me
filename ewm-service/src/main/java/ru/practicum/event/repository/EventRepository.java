package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.id IN :ids " +
            "ORDER BY e.id")
    Set<Event> findEventsByIds(@Param("ids") Long[] eventIds);

    Optional<Event> findEventByIdAndState(Long eventId, EventState published);

    List<Event> findEventsByInitiatorId(Long userId, Pageable page);

    Optional<Event> findEventByInitiatorIdAndId(Long userId, Long eventId);

    @Modifying
    @Query(value = "" +
            "UPDATE Event e " +
            "SET e.confirmedRequests = :requests " +
            "WHERE e.id = :id")
    void setEventConfirmedRequests(@Param ("id") Long eventId, @Param("requests") Integer confirmedRequests);
}
