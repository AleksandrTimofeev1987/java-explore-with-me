package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import ru.practicum.category.entity.Category;
import ru.practicum.event.entity.EventState;
import ru.practicum.event.entity.Location;
import ru.practicum.user.dto.UserResponseIdAndName;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventResponseFull {

    private Long id;

    private String title;

    private String annotation;

    private String description;

    private Category category;

    private LocalDateTime eventDate;

    private UserResponseIdAndName initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private Integer views;

    private Integer confirmedRequests;

    private EventState state;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private Double rate;
}
