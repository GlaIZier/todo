package ru.glaizier.todo.model.dto.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.glaizier.todo.model.dto.api.Link;


@Value
@RequiredArgsConstructor
public class OutputData<T> {
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_link")
    private final Link link;

    public OutputData(T data) {
        this(data, null);
    }
}
