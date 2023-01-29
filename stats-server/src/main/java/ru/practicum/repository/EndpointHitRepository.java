package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.entity.EndpointHit;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
