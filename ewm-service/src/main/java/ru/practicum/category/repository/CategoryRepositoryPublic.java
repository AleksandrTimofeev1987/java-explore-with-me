package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.entity.Category;

@Repository
public interface CategoryRepositoryPublic extends JpaRepository<Category, Long> {
}
