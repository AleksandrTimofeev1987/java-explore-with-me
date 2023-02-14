package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private Double rate;
}
