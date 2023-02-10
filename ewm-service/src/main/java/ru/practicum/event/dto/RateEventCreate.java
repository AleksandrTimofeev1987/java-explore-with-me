package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RateEventCreate {

    private Long id;

    private Long user;

    @NotNull(message = "{event.rate_event.not_null}")
    private Long event;

    @Min(value = 1, message = "{rate.rate_event.min}")
    @Max(value = 5, message = "{rate.rate_event.max}")
    private Integer rate;

}
