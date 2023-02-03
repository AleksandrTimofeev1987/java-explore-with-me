package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.request.entity.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findRequestByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findRequestByRequesterId(Long userId);

    // TODO
    List<Request> findRequestByEventId(Long eventId);

    @Query(value = "" +
            "SELECT r " +
            "FROM Request r " +
            "WHERE r.event.id = :eventId AND " +
            "r.id IN :requestIds")
    List<Request> findRequestsForUpdate(@Param("eventId") Long eventId, @Param("requestIds") Long[] requestIds);
}
