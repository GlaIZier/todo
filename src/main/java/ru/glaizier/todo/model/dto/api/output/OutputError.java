package ru.glaizier.todo.model.dto.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.glaizier.todo.model.dto.api.HttpResponse;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class OutputError {
    public static final OutputError BAD_REQUEST = new OutputError(new HttpResponse(400, "Wrong provided request data!"));
    public static final OutputError UNAUTHORIZED = new OutputError(new HttpResponse(401, "Unauthorized!"));
    public static final OutputError FORBIDDEN = new OutputError(new HttpResponse(403, "Forbidden!"));
    public static final OutputError NOT_FOUND = new OutputError(new HttpResponse(404, "Data hasn't been found!"));
    public static final OutputError INTERNAL_SERVER_ERROR = new OutputError(new HttpResponse(500, "Internal server error!"));

    private final HttpResponse error;
}
