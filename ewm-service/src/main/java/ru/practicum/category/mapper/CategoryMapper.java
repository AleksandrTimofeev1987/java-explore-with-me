package ru.practicum.category.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.category.dto.CategoryCreate;
import ru.practicum.category.dto.CategoryResponse;
import ru.practicum.category.dto.CategoryUpdate;
import ru.practicum.category.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponseDto(Category category);

    Category toCategoryEntity(CategoryCreate categoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toCategoryFromCategoryUpdate(CategoryUpdate categoryDto, @MappingTarget Category category);
}
