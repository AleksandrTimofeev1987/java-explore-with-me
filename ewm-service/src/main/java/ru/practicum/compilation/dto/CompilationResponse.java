package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.dto.EventResponseShort;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationResponse {

    private Long id;

    private List<EventResponseShort> events;

    private Boolean pinned;

    private String title;
}
