package ru.glaizier.todo.controller.api.user;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.glaizier.todo.domain.Role.USER;

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
import ru.glaizier.todo.dao.UserDao;
import ru.glaizier.todo.domain.User;
import ru.glaizier.todo.domain.api.Error;
import ru.glaizier.todo.domain.api.input.InputUser;
import ru.glaizier.todo.domain.api.output.OutputData;
import ru.glaizier.todo.domain.api.output.OutputError;

import java.lang.invoke.MethodHandles;
import java.util.Collections;

// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
@RestController
@RequestMapping(value = {"/api/v1/users", "/api/users"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDao userDao;

    /**
     * Exceptions
     */
    // Todo add mdc and logging aspects here to handle exceptionHandlers?
    // Todo make BaseController class and move general exceptions handlers there
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

    /**
     * Methods
     */

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<String>> registerUser(InputUser inputUser) {
        checkUserIsNotEmpty(inputUser);
        User createdUser = User.builder().login(inputUser.getLogin()).password(inputUser.getPassword())
                .roles(Collections.singletonList(USER)).build();
        userDao.addUser(createdUser);

        OutputData<String> outputData = new OutputData<>(createdUser.getLogin(), null);
        return new ResponseEntity<>(outputData, HttpStatus.CREATED);
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