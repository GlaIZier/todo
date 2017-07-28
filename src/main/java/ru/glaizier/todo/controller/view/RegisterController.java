package ru.glaizier.todo.controller.view;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.glaizier.todo.model.dto.api.input.InputUser;
import ru.glaizier.todo.persistence.Persistence;

import java.lang.invoke.MethodHandles;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Persistence persistence;

    @Autowired
    public RegisterController(Persistence persistence) {
        this.persistence = persistence;
    }

    @RequestMapping("/register")
    public String show() {
        return "register";
    }

    @RequestMapping(value = "/register", method = POST)
    // Todo add validation, check scrf here
    public String register(
            @Valid InputUser inputUser,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info(errors.getAllErrors().get(0).getDefaultMessage());
            return "register";
        }
        persistence.saveUser(inputUser.getLogin(), inputUser.getPassword());
        return "redirect:/";
    }

}
