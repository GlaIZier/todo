package ru.glaizier.todo.model.dto.api.input;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class InputUser {
    @NotBlank
    @Size(min = 1, max = 30)
    private String login;
    @NotNull
    @Size(min = 1, max = 30)
    private char[] password;
}
