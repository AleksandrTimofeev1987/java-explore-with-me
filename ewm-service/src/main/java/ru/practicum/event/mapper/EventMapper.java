package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.entity.Category;
import ru.practicum.event.dto.EventCreate;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.EventState;
import ru.practicum.user.entity.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface EventMapper {

    default Event toEventEntity(Long userId, EventCreate eventDto) {
        if (eventDto == null) {
            return null;
        }

        Event event = new Event();
        Category category = new Category();
        category.setId(eventDto.getCategory());
        User user = new User();
        user.setId(userId);

        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setCategory(category);
        event.setEventDate(eventDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(eventDto.getLocation());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setCreatedOn(LocalDateTime.now());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setViews(0);
        event.setConfirmedRequests(0);
        event.setState(EventState.PENDING);

        return event;
    }
}
