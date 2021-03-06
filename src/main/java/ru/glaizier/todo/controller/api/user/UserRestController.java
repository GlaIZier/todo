package ru.glaizier.todo.controller.api.user;

import java.util.Collections;
import java.util.HashSet;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.glaizier.todo.controller.api.exception.ApiBadRequestException;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.model.dto.api.output.OutputData;
import ru.glaizier.todo.model.dto.api.output.OutputUser;
import ru.glaizier.todo.model.dto.input.InputUser;
import ru.glaizier.todo.persistence.Persistence;

@RestController
@RequestMapping(value = {"/api/v1/users", "/api/users"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class UserRestController extends ExceptionHandlingController {

    private final Persistence persistence;

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<OutputUser>> registerUser(@Valid InputUser inputUser,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ApiBadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());

        UserDto userDto = persistence.saveUser(inputUser.getLogin(), inputUser.getPassword(),
                new HashSet<>(Collections.singletonList(new RoleDto(Role.USER.getRole()))));


        OutputData<OutputUser> outputData = new OutputData<>(new OutputUser(userDto.getLogin()));
        return new ResponseEntity<>(outputData, HttpStatus.CREATED);
    }


    public static void checkUserIsNotEmpty(InputUser inputUser) {
        if (inputUser == null)
            throw new ApiBadRequestException("Provided user is null!");
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(inputUser.getLogin())))
            throw new ApiBadRequestException("Provided user login is empty or null!");
        if (inputUser.getPassword() == null ||
                StringUtils.isEmpty(StringUtils.trimWhitespace(String.valueOf(inputUser.getPassword()))))
            throw new ApiBadRequestException("Provided user password is empty or null!");
    }

}