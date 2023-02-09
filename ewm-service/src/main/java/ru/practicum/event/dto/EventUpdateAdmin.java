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
public class EventUpdateAdmin {

    @Null
    private Long id;

    @Size(min = 3, max = 120, message = "{title.event.length}")
    private String title;

    @Size(min = 20, max = 2000, message = "{annotation.event.length}")
    private String annotation;

    @Size(min = 20, max = 7000, message = "{description.event.length}")
    private String description;

    @Positive(message = "{category.event.positive}")
    private Long category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero(message = "{participant_limit.event.positive_or_zero}")
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionAdmin stateAction;
}
