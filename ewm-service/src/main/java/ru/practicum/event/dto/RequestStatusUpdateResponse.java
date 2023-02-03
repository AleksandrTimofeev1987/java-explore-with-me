package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.RequestResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusUpdateResponse {

    List<RequestResponse> confirmedRequests;

    List<RequestResponse> rejectedRequests;

}
