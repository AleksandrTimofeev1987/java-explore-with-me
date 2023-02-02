package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.entity.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Valid
public class EventCreate {

    private Long id;

    @NotBlank(message = "Event title should not be Null or Blank")
    @Size(min = 3, max = 120, message = "Event title length should be from 3 to 120 characters")
    private String title;

    @NotBlank(message = "Event annotation should not be Null or Blank")
    @Size(min = 20, max = 2000, message = "Event annotation length should be from 20 to 2000 characters")
    private String annotation;

    @NotBlank(message = "Event description should not be Null or Blank")
    @Size(min = 20, max = 7000, message = "Event description length should be from 20 to 7000 characters")
    private String description;

    @NotNull(message = "Event category should not be Null")
    @Positive(message = "Event category reference should be a positive number")
    private Long category;

    @NotNull(message = "Event date should not be Null")
    @Future(message = "Event date should be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Event location should not be Null")
    private Location location;

    private Boolean paid;

    @PositiveOrZero(message = "Event participant limit should be 0 or more")
    private Integer participantLimit;

    private Boolean requestModeration;
}
