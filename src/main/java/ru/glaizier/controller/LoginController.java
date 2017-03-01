package ru.glaizier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    // TODO start. Add here error login message. Use ModelAndView to return data to view
    public String login() {
        return "login";
    }

}
