package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepositoryAdmin extends JpaRepository<Event, Long> {

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end ")
    List<Event> findByRange(@Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.initiator.id IN :users")
    List<Event> findByUsers(@Param("users") Long[] users, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.state IN :states")
    List<Event> findByStates(@Param("states") EventState[] states, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.category.id IN :categories")
    List<Event> findByCategories(@Param("categories") Long[] categories, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.initiator.id IN :users AND " +
            "e.state IN :states")
    List<Event> findByUsersAndStates(@Param("users") Long[] users, @Param("states") EventState[] states, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.initiator.id IN :users AND " +
            "e.category.id IN :categories")
    List<Event> findByUsersAndCategories(@Param("users") Long[] users, @Param("categories") Long[] categories, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.state IN :states AND " +
            "e.category.id IN :categories")
    List<Event> findByStatesAndCategories(@Param("states") EventState[] states, @Param("categories") Long[] categories, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "" +
            "SELECT e " +
            "FROM Event e " +
            "WHERE e.eventDate > :start AND " +
            "e.eventDate < :end AND " +
            "e.initiator.id IN :users AND " +
            "e.state IN :states AND " +
            "e.category.id IN :categories")
    List<Event> findByUsersAndStatesAndCategories(@Param("users") Long[] users, @Param("states") EventState[] states, @Param("categories") Long[] categories, @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd, Pageable page);
}
