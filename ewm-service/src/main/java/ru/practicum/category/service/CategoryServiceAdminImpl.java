package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.dto.CategoryUpdate;
import ru.practicum.category.entity.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepositoryAdmin;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryRepositoryAdmin repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponse createCategory(Category category) {
        log.debug("Request to add category with name={} is received.", category.getName());

        Category createdCategory;
        try {
            createdCategory = repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category name is a duplicate.");
        }

        log.debug("Category with ID={} is added to repository.", createdCategory.getId());
        return mapper.toCategoryResponseDto(createdCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        log.debug("Request to delete category with ID={} is received.", id);

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Category with id=%d is not found", id));
        }

        log.debug("Category with ID={} is deleted from repository.", id);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdate categoryDto) {
        log.debug("Request to update category with ID={} is received.", id);
        Category categoryForUpdate = repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Category with id=%d is not found", id)));
        mapper.toCategoryFromCategoryUpdate(categoryDto, categoryForUpdate);

        Category updatedCategory;
//        try {
            updatedCategory = repository.save(categoryForUpdate);
//        } catch (DataIntegrityViolationException e) {
//            throw new ConflictException("Category name is a duplicate.");
//        }

        log.debug("Category with ID={} is updated in repository.", id);
        return mapper.toCategoryResponseDto(updatedCategory);
    }
}
