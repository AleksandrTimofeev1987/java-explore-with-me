package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.category.entity.Category;
import ru.practicum.user.dto.UserResponseIdAndName;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private String annotation;
    private Category category;
    private LocalDateTime eventDate;
    private UserResponseIdAndName initiator;
    private Boolean paid;
    private Integer views;
    private Integer confirmedRequests;
}
