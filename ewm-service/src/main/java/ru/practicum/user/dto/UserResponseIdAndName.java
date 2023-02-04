package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseIdAndName {

    private Long id;

    private String name;
}
