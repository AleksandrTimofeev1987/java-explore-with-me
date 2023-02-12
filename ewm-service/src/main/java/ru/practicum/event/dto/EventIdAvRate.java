package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventIdAvRate {

    Long eventId;

    Double rate;

}
