package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.Event;
import java.util.Set;

@Repository
public interface EventRepositoryAdmin extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.id IN :ids " +
            "ORDER BY e.id")
    Set<Event> findEventsByIds(@Param("ids") Long[] eventIds);
}
