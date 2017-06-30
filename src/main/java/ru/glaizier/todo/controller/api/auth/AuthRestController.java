package ru.glaizier.todo.controller.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.dao.Persistence;
import ru.glaizier.todo.security.token.TokenService;

// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
//@RestController
@RequestMapping(value = {"/api/v1/auth", "/api/auth"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class AuthRestController extends ExceptionHandlingController {

    private final Persistence persistence;

    private final TokenService tokenService;

    /*
    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<OutputUser>> authenticateUser(InputUser inputUser) {
        UserRestController.checkUserIsNotEmpty(inputUser);

        if (persistence.getUserDao().findUserByLogin(inputUser.getLogin()) == null)
            throw new ApiNotFoundException(format("User with login %s wasn't found!", inputUser.getLogin()));

        if (persistence.getUserDao().findUserByLoginAndPassword(inputUser.getLogin(), inputUser.getPassword()) == null)
            throw new ApiUnauthorizedException("Wrong credentials were provided!");

        String token = tokenService.createToken(inputUser.getLogin());

        OutputData<OutputUser> outputData = new OutputData<>(new OutputUser(inputUser.getLogin(), token));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }
    */
}
