package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryCreate;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.dto.CategoryUpdate;
import ru.practicum.category.entity.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryServiceAdminImpl;
import ru.practicum.exception.model.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryControllerAdmin {

    private final CategoryServiceAdminImpl service;
    private final CategoryMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryCreate categoryDto) {
        log.debug("Creating category with name={}", categoryDto.getName());
        Category category = mapper.toCategoryEntity(categoryDto);
        return service.createCategory(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable Long catId) {
        log.debug("Deleting category with id={}", catId);
        service.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponse updateItem(@Positive @PathVariable Long catId,
                                       @RequestBody @NotNull CategoryUpdate categoryDto) {
        log.debug("Updating category with id={}", catId);
        verifyCategoryDto(categoryDto);
        return service.updateCategory(catId, categoryDto);
    }

    private void verifyCategoryDto(CategoryUpdate categoryDto) {
        if (categoryDto.getName() == null) {
            throw new BadRequestException("Parameters of the update object should not be null");
        }
    }
}
