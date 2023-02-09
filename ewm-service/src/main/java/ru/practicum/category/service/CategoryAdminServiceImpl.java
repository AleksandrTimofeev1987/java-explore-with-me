package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.dto.CategoryUpdate;
import ru.practicum.category.entity.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final MessageSource messageSource;

    @Override
    public CategoryResponse createCategory(Category category) {
        log.debug("Request to add category with name={} is received.", category.getName());

        Category createdCategory;
        try {
            createdCategory = repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("name.category.duplicate", null, null));
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
            throw new NotFoundException(String.format(messageSource.getMessage("category.not_found", new Object[] {id}, null)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("category.delete.used_in_events", new Object[] {id}, null));
        }

        log.debug("Category with ID={} is deleted from repository.", id);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdate categoryDto) {
        log.debug("Request to update category with ID={} is received.", id);
        Category categoryForUpdate = repository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not_found", new Object[] {id}, null)));
        mapper.toCategoryFromCategoryUpdate(categoryDto, categoryForUpdate);

        Category updatedCategory;
        try {
            updatedCategory = repository.save(categoryForUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(messageSource.getMessage("name.category.duplicate", null, null));
        }

        log.debug("Category with ID={} is updated in repository.", id);
        return mapper.toCategoryResponseDto(updatedCategory);
    }
}
