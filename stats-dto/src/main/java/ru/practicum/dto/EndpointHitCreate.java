package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EndpointHitCreate {

    private Long id;

    @NotBlank(message = "App name should not be Null or Blank")
    private String app;

    @NotBlank (message = "Uri should not be Null or Blank")
    private String uri;

    @NotBlank (message = "User ip should not be Null or Blank")
    private String ip;

    @NotNull(message = "Time stamp should not be Null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
