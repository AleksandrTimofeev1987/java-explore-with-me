package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestStatusUpdateRequest {

    private Long[] requestIds;

    private StateRequestStatus status;

}
