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

        return new RateEventResponse(rateEvent.getId(),
                rateEvent.getUser().getId(),
                rateEvent.getEvent().getId(),
                rateEvent.getRate());
    }
}
