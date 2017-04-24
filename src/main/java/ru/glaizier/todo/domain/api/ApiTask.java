package ru.glaizier.todo.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.glaizier.todo.domain.Task;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
// include only if it is needed during service usage
@Deprecated
public class ApiTask {
    private final Task task;
}
