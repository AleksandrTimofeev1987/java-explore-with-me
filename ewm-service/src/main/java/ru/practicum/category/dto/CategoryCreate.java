package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Valid
public class CategoryCreate {

    private Long id;

    @NotBlank(message = "Category name should not be Null or Blank")
    private String name;
}
