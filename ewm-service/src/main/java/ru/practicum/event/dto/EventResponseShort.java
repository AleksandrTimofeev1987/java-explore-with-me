package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import ru.practicum.category.entity.Category;
import ru.practicum.user.dto.UserResponseIdAndName;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventResponseShort {
    private Long id;
    private String title;
    private String annotation;
    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserResponseIdAndName initiator;
    private Boolean paid;
    private Integer views;
    private Integer confirmedRequests;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private Double rate;
}
