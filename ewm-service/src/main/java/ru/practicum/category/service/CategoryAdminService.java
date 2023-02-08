package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.dto.CategoryUpdate;
import ru.practicum.category.entity.Category;

public interface CategoryAdminService {

    /**
     * Method adds category to repository.
     *
     * @param category User to be added.
     *
     * @return Added category with assigned ID.
     */
    CategoryResponse createCategory(Category category);

    /**
     * Method deletes category in repository.
     *
     * @param id ID of category to be deleted.
     */
    void deleteCategory(Long id);

    /**
     * Method updates category in repository.
     *
     * @param id ID of category to be updated.
     * @param categoryDto Category with updated fields.
     *
     * @return Updated category.
     */
    CategoryResponse updateCategory(Long id, CategoryUpdate categoryDto);
}
