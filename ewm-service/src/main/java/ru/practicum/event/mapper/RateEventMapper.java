package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import ru.practicum.event.dto.RateEventResponse;
import ru.practicum.event.entity.RateEvent;

@Mapper(componentModel = "spring")
public interface RateEventMapper {

    default RateEventResponse toRateEventResponse(RateEvent rateEvent) {
        if (rateEvent == null) {
            return null;
        }

        Long id = rateEvent.getId();

        Long user = rateEvent.getUser().getId();

        Long event = rateEvent.getEvent().getId();

        Integer rate = rateEvent.getRate();

        return new RateEventResponse(id, user, event, rate);
    }
}
