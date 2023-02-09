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

    @NotBlank(message = "{title.compilation.not_blank}")
    private String title;

    @NotNull(message = "{events.compilation.not_null}")
    private Long[] events;

    @NotNull(message = "{pinned.compilation.not_null}")
    private Boolean pinned;
}
