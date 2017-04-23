package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class Error {

    private final int code;

    private final String message;

    @RequiredArgsConstructor
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum DefinedError {
        UNAUTHORIZED(401, "Unauthorized");

        @JsonProperty("code")
        private final int code;

        @JsonProperty("message")
        private final String message;

    }

}
