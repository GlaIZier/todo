package ru.glaizier.todo.controller.view;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.glaizier.todo.model.dto.input.InputUser;
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
    public String show(Model model) {
        InputUser userDto = new InputUser();
        model.addAttribute("user", userDto);
        return "register";
    }

    @RequestMapping(value = "/register", method = POST)
    // Todo add validation, check scrf here
    public ModelAndView register(
            @ModelAttribute("user") @Valid InputUser inputUser,
            Errors errors,
            BindingResult result) {
        if (!errors.hasErrors()) {
            if (persistence.findUser(inputUser.getLogin()) != null)
                result.rejectValue("login", "message.regError", "This user already exists!");
        }

        if (errors.hasErrors())
            return new ModelAndView("register", "user", inputUser);

        persistence.saveUser(inputUser.getLogin(), inputUser.getPassword());
        return new ModelAndView("redirect:/");
    }

}
