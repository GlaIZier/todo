package ru.glaizier.todo.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
// Todo make link auto creatable
public class ApiData<T> {
    private final T data;
    @JsonProperty("_link")
    private final Link link;
}
