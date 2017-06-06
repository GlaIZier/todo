package ru.glaizier.todo.domain.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.glaizier.todo.domain.api.Link;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
@RequiredArgsConstructor
public class OutputData<T> {
    private final T data;
    @JsonProperty("_link")
    private final Link link;

    public OutputData(T data) {
        this(data, null);
    }
}