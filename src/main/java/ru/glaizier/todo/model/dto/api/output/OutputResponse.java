package ru.glaizier.todo.model.dto.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.glaizier.todo.model.dto.api.HttpResponse;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class OutputResponse {
    public static final OutputResponse OK = new OutputResponse(new HttpResponse(200, "OK"));

    private final HttpResponse response;
}
