package ru.glaizier.todo.model.dto.input;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class InputUser {
    @NotBlank(message = "Login must not be blank!")
    @Size(min = 1, max = 30, message = "Login '${validatedValue}' must be between {min} and {max} characters long!")
    private String login;
    @NotNull(message = "Password must not be blank!")
    @Size(min = 1, max = 30, message = "Password must be between {min} and {max} characters long!")
    private char[] password;
}
