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

    @NotBlank(message = "{name.category.not_blank}")
    private String name;
}
