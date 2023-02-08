package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryPublicService {

    /**
     * Method returns all categories paginated.
     *
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of categories.
     */
    List<CategoryResponse> getCategories(Integer from, Integer size);

    /**
     * Method returns category by ID.
     *
     * @param catId ID of category to select.
     *
     * @return Category with this ID.
     */
    CategoryResponse getCategoryById(Long catId);
}
