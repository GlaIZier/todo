package ru.glaizier.todo.domain.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ru.glaizier.todo.domain.api.Link;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
// Todo make link auto creatable
public class OutputData<T> {
    private final T data;
    @JsonProperty("_link")
    private final Link link;
}
