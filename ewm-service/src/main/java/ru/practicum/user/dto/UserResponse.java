package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private Double rate;
}
