package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Valid
public class CompilationCreate {

    @NotBlank(message = "Compilation title should not be Null or Blank")
    private String title;

    @NotNull(message = "Events should not be Null")
    private Long[] events;

    @NotNull(message = "Pinned should not be Null")
    private Boolean pinned;
}
