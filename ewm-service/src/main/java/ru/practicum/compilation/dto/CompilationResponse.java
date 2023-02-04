package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.dto.EventResponse;

import java.util.Set;

@Data
@AllArgsConstructor
public class CompilationResponse {

    private Long id;

    private Set<EventResponse> events;

    private Boolean pinned;

    private String title;
}
