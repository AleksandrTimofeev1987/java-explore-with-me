package ru.practicum.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.entity.Compilation;

import java.util.List;

@Repository
public interface CompilationRepositoryPublic extends JpaRepository<Compilation, Long> {

    List<Compilation> findCompilationByPinned(Boolean pinned, Pageable page);
}
