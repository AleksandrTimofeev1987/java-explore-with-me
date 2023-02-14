package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.request.dto.RequestResponse;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestStatusUpdateResponse {

    List<RequestResponse> confirmedRequests;

    List<RequestResponse> rejectedRequests;

    public RequestStatusUpdateResponse() {
        this.confirmedRequests = new ArrayList<>();
        this.rejectedRequests = new ArrayList<>();
    }

}
