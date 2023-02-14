package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Valid
public class UserCreate {

    private Long id;

    @NotBlank(message = "{name.user.not_blank}")
    private String name;

    @Email(message = "{email.user.not_valid}")
    @NotBlank (message = "{email.user.not_blank}")
    private String email;
}
