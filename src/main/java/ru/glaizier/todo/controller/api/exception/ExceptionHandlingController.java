package ru.glaizier.todo.controller.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.model.dto.api.Error;
import ru.glaizier.todo.model.dto.api.output.OutputError;

import java.lang.invoke.MethodHandles;

public class ExceptionHandlingController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Exceptions
     */
    // Todo add mdc and logging aspects here to handle exceptionHandlers?
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputError> handleException(Exception e) {
        log.error("Request to rest controller failed with unexpected error: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.INTERNAL_SERVER_ERROR.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiBadRequestException.class)
    public ResponseEntity<OutputError> handleBadRequestException(
            ApiBadRequestException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.BAD_REQUEST.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApiNotFoundException.class, ApiTaskNotFoundException.class})
    public ResponseEntity<OutputError> handleNotFoundException(
            ApiNotFoundException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.NOT_FOUND.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiUnauthorizedException.class)
    public ResponseEntity<OutputError> handleUnauthorizedException(
            ApiUnauthorizedException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.UNAUTHORIZED.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ApiForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<OutputError> handleForbiddenException(
            ApiUnauthorizedException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.FORBIDDEN.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.FORBIDDEN);
    }

}
