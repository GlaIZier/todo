package ru.glaizier.todo.controller.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.dao.Persistence;

// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
//@RestController
@RequestMapping(value = {"/api/v1/users", "/api/users"})
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class UserRestController extends ExceptionHandlingController {

    private final Persistence persistence;

    /*
    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<OutputData<String>> registerUser(InputUser inputUser) {
        checkUserIsNotEmpty(inputUser);
        User createdUser = User.builder().login(inputUser.getLogin()).password(inputUser.getPassword())
                .roles(new HashSet<>(Collections.singletonList(Role.USER))).build();
//        persistence.getUserDao().save(createdUser);

        OutputData<String> outputData = new OutputData<>(createdUser.getLogin());
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
    */
}