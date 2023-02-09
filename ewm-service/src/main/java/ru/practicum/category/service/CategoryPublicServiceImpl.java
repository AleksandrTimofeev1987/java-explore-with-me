package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.entity.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicServiceImpl implements CategoryPublicService {

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final MessageSource messageSource;

    @Override
    public List<CategoryResponse> getCategories(Integer from, Integer size) {
        log.debug("A list of categories is requested with the following pagination parameters: from={} and size={}.", from, size);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
        List<Category> foundCategories = repository.findAll(page).getContent();

        log.debug("A list of categories is received from repository with size of {}.", foundCategories.size());
        return foundCategories
                .stream()
                .map(mapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long catId) {
        log.debug("Category with id={} is requested.", catId);
        Category foundCategory = repository.findById(catId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not_found", new Object[] {catId}, null)));
        log.debug("Category with id={} is received from repository.", catId);
        return mapper.toCategoryResponseDto(foundCategory);
    }
}
