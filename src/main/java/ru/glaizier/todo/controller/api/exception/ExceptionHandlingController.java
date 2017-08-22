package ru.glaizier.todo.controller.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.glaizier.todo.model.dto.api.HttpResponse;
import ru.glaizier.todo.model.dto.api.output.OutputError;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

public class ExceptionHandlingController {


    /**
     * Exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputError> handleException(Exception e) {
        OutputError outputError = new OutputError(new HttpResponse(OutputError.INTERNAL_SERVER_ERROR.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiBadRequestException.class)
    public ResponseEntity<OutputError> handleBadRequestException(
            ApiBadRequestException e) {

        OutputError outputError = new OutputError(new HttpResponse(OutputError.BAD_REQUEST.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApiNotFoundException.class, ApiTaskNotFoundException.class})
    public ResponseEntity<OutputError> handleNotFoundException(
            ApiNotFoundException e) {
        OutputError outputError = new OutputError(new HttpResponse(OutputError.NOT_FOUND.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiUnauthorizedException.class)
    public ResponseEntity<OutputError> handleUnauthorizedException(
            ApiUnauthorizedException e) {
        OutputError outputError = new OutputError(new HttpResponse(OutputError.UNAUTHORIZED.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<OutputError> handleForbiddenException(
            AccessDeniedException e) {
        return handleForbiddenException(new ApiForbiddenException(e.getMessage(), e));
    }

    @ExceptionHandler(ApiForbiddenException.class)
    public ResponseEntity<OutputError> handleForbiddenException(
            ApiForbiddenException e) {
        OutputError outputError = new OutputError(new HttpResponse(OutputError.FORBIDDEN.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.FORBIDDEN);
    }

}
