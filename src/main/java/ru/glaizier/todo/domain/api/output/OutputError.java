package ru.glaizier.todo.domain.api.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.glaizier.todo.domain.api.Error;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class OutputError {
    public static final OutputError BAD_REQUEST = new OutputError(new Error(400, "Wrong provided request data!"));
    public static final OutputError UNAUTHORIZED = new OutputError(new Error(401, "Unauthorized!"));
    public static final OutputError FORBIDDEN = new OutputError(new Error(403, "Forbidden!"));
    public static final OutputError NOT_FOUND = new OutputError(new Error(404, "Data hasn't been found!"));
    public static final OutputError INTERNAL_SERVER_ERROR = new OutputError(new Error(500, "Internal server error!"));

    private final Error error;
}
