package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@Valid
public class CategoryUpdate {

    @Null
    private Long id;

    @NotBlank(message = "Category name should not be Null or Blank")
    private String name;
}
