package ru.glaizier.todo.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class ApiError {
    public static final ApiError UNAUTHORIZED = new ApiError(new Error(401, "Unauthorized!"));
    public static final ApiError BAD_REQUEST = new ApiError(new Error(400, "Wrong provided request data!"));

    private final Error error;
}
