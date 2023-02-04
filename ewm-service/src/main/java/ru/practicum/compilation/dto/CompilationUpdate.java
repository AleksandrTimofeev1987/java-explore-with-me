package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompilationUpdate {

    private String title;

    private Long[] events;

    private Boolean pinned;
}
