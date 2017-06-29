package ru.glaizier.todo.controller.api.auth;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glaizier.todo.controller.api.exception.ApiNotFoundException;
import ru.glaizier.todo.controller.api.exception.ApiUnauthorizedException;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.controller.api.user.UserRestController;
import ru.glaizier.todo.dao.Dao;
import ru.glaizier.todo.model.dto.api.input.InputUser;
import ru.glaizier.todo.model.dto.api.output.OutputData;
import ru.glaizier.todo.model.dto.api.output.OutputUser;
import ru.glaizier.todo.security.token.TokenService;

// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
@RestController
@RequestMapping(value = {"/api/v1/auth", "/api/auth"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class AuthRestController extends ExceptionHandlingController {

    private final Dao dao;

    private final TokenService tokenService;

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<OutputUser>> authenticateUser(InputUser inputUser) {
        UserRestController.checkUserIsNotEmpty(inputUser);

        if (dao.getUserDao().findUserByLogin(inputUser.getLogin()) == null)
            throw new ApiNotFoundException(format("User with login %s wasn't found!", inputUser.getLogin()));

        if (dao.getUserDao().findUserByLoginAndPassword(inputUser.getLogin(), inputUser.getPassword()) == null)
            throw new ApiUnauthorizedException("Wrong credentials were provided!");

        String token = tokenService.createToken(inputUser.getLogin());

        OutputData<OutputUser> outputData = new OutputData<>(new OutputUser(inputUser.getLogin(), token));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }
}
