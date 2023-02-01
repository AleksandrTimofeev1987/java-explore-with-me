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

    @NotBlank(message = "User name should not be Null or Blank")
    private String name;

    @Email(message = "User email should be valid")
    @NotBlank (message = "User email should not be Null or Blank")
    private String email;
}
