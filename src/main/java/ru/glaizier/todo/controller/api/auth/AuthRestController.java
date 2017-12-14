package ru.glaizier.todo.controller.api.auth;

import static java.lang.String.format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.glaizier.todo.controller.api.exception.ApiBadRequestException;
import ru.glaizier.todo.controller.api.exception.ApiNotFoundException;
import ru.glaizier.todo.controller.api.exception.ApiUnauthorizedException;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.model.dto.api.output.OutputAuthUser;
import ru.glaizier.todo.model.dto.api.output.OutputData;
import ru.glaizier.todo.model.dto.api.output.OutputResponse;
import ru.glaizier.todo.model.dto.input.InputUser;
import ru.glaizier.todo.persistence.Persistence;
import ru.glaizier.todo.security.token.TokenService;

@RestController
@RequestMapping(value = {"/api/v1/auth", "/api/auth"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class AuthRestController extends ExceptionHandlingController {

    private final Persistence persistence;

    private final TokenService tokenService;

    private final LogoutHandler logoutHandler;

    @RequestMapping(value = "/login", method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<OutputAuthUser>> loginUser(@Valid InputUser inputUser,
                                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ApiBadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());

        if (persistence.findUser(inputUser.getLogin()) == null)
            throw new ApiNotFoundException(format("User with login %s wasn't found!", inputUser.getLogin()));

        if (persistence.findUser(inputUser.getLogin(), inputUser.getPassword()) == null)
            throw new ApiUnauthorizedException("Wrong credentials were provided!");

        String token = tokenService.createToken(inputUser.getLogin());

        OutputData<OutputAuthUser> outputData = new OutputData<>(new OutputAuthUser(inputUser.getLogin(), token));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/me/logout", method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE/*,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}*/)
    public ResponseEntity<OutputResponse> logoutUser(HttpServletRequest req, HttpServletResponse resp, Authentication auth) {
        logoutHandler.logout(req, resp, auth);
        return new ResponseEntity<>(OutputResponse.OK, HttpStatus.OK);
    }
}
