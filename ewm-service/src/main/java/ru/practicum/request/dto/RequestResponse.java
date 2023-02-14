package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.request.entity.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestResponse {

    private Long id;

    private Long requester;

    private Long event;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private RequestStatus status;

}
