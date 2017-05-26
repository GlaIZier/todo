package ru.glaizier.todo.controller.api.auth;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glaizier.todo.controller.api.exception.ApiBadRequestException;
import ru.glaizier.todo.controller.api.exception.ApiNotFoundException;
import ru.glaizier.todo.controller.api.exception.ApiUnauthorizedException;
import ru.glaizier.todo.dao.UserDao;
import ru.glaizier.todo.domain.api.Error;
import ru.glaizier.todo.domain.api.input.InputUser;
import ru.glaizier.todo.domain.api.output.OutputData;
import ru.glaizier.todo.domain.api.output.OutputError;
import ru.glaizier.todo.domain.api.output.OutputUser;
import ru.glaizier.todo.security.token.TokenService;

import java.lang.invoke.MethodHandles;

// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
@RestController
@RequestMapping(value = {"/api/v1/auth", "/api/auth"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class AuthRestController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDao userDao;

    private final TokenService tokenService;

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

    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<OutputError> handleNotFoundException(
            ApiNotFoundException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.NOT_FOUND.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiUnauthorizedException.class)
    public ResponseEntity<OutputError> handleNotFoundException(
            ApiUnauthorizedException e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);

        OutputError outputError = new OutputError(new Error(OutputError.UNAUTHORIZED.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(outputError, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<OutputUser>> authenticateUser(InputUser inputUser) {
        checkUserIsNotEmpty(inputUser);

        if (!userDao.containsUser(inputUser.getLogin()))
            throw new ApiNotFoundException(format("User with login %s wasn't found!", inputUser.getLogin()));

        if (userDao.getUserWithPassword(inputUser.getLogin(), inputUser.getPassword()) == null)
            throw new ApiUnauthorizedException("Wrong credentials were provided!");

        String token = tokenService.createToken(inputUser.getLogin());

        OutputData<OutputUser> outputData = new OutputData<>(new OutputUser(inputUser.getLogin(), token), null);
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    private void checkUserIsNotEmpty(InputUser inputUser) {
        if (inputUser == null)
            throw new ApiBadRequestException("Provided user is null!");
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(inputUser.getLogin())))
            throw new ApiBadRequestException("Provided user login is empty or null!");
        if (inputUser.getPassword() == null ||
                StringUtils.isEmpty(StringUtils.trimWhitespace(String.valueOf(inputUser.getPassword()))))
            throw new ApiBadRequestException("Provided user password is empty or null!");
    }

}
