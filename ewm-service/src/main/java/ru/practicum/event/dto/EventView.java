package ru.practicum.event.dto;

import ru.practicum.category.entity.Category;
import ru.practicum.user.dto.UserView;

import java.time.LocalDateTime;

public interface EventView {

    Long getId();
    String getTitle();
    String getAnnotation();
    Category getCategory();
    LocalDateTime getEventDate();
    UserView getInitiator();
    Boolean getPaid();
    Integer getViews();
    Integer getConfirmedRequests();

}
