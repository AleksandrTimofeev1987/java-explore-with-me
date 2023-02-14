package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.EndpointHitCreate;
import ru.practicum.dto.EndpointHitResponse;
import ru.practicum.entity.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    EndpointHitResponse toEndpointHitResponse(EndpointHit hit);

    EndpointHit toEndpointHitEntity(EndpointHitCreate hitDto);
}
