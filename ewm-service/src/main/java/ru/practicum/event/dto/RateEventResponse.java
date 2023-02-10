package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateEventResponse {

    private Long id;

    private Long user;

    private Long event;

    private Integer rate;
}
