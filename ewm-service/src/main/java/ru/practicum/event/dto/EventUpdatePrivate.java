package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.entity.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EventUpdatePrivate {

    @Null
    private Long id;

    @Size(min = 3, max = 120, message = "Event title length should be from 3 to 120 characters")
    private String title;

    @Size(min = 20, max = 2000, message = "Event annotation length should be from 20 to 2000 characters")
    private String annotation;

    @Size(min = 20, max = 7000, message = "Event description length should be from 20 to 7000 characters")
    private String description;

    @Positive(message = "Event category reference should be a positive number")
    private Long category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero(message = "Event participant limit should be 0 or more")
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionPrivate stateAction;
}
