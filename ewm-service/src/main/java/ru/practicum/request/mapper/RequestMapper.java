package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.request.dto.RequestResponse;
import ru.practicum.request.entity.Request;
import ru.practicum.request.entity.RequestStatus;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    default RequestResponse toRequestResponse(Request request) {
        if (request == null) {
            return null;
        }

        Long id = request.getId();

        Long requester = request.getRequester().getId();

        Long event = request.getEvent().getId();

        LocalDateTime created = request.getCreated();

        RequestStatus status = request.getStatus();

        return new RequestResponse(id, requester, event, created, status);
    }

}
