package ru.glaizier.todo.domain.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
public class OutputTask {
    private Integer id;
    private String login;
    private String todo;
}
