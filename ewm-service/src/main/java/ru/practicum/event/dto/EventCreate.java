package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.entity.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EventCreate {

    private Long id;

    @NotBlank(message = "{title.event.not_blank}")
    @Size(min = 3, max = 120, message = "{title.event.length}")
    private String title;

    @NotBlank(message = "{annotation.event.not_blank}")
    @Size(min = 20, max = 2000, message = "{annotation.event.length}")
    private String annotation;

    @NotBlank(message = "{description.event.not_blank}")
    @Size(min = 20, max = 7000, message = "{description.event.length}")
    private String description;

    @NotNull(message = "{category.event.not_null}")
    @Positive(message = "{category.event.positive}")
    private Long category;

    @NotNull(message = "{date.event.not_null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "{location.event.not_null}")
    private Location location;

    private Boolean paid;

    @PositiveOrZero(message = "{participant_limit.event.positive_or_zero}")
    private Integer participantLimit;

    private Boolean requestModeration;
}
