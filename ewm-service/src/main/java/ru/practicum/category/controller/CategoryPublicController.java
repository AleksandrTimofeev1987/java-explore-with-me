package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.service.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryPublicController {

    private final CategoryPublicService service;

    @GetMapping
    public List<CategoryResponse> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Getting categories");
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponse getCategoryById(@PathVariable @PositiveOrZero Long catId) {
        log.debug("Getting category with id={}", catId);
        return service.getCategoryById(catId);
    }
}
