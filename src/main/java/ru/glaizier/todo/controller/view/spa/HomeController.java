package ru.glaizier.todo.controller.view.spa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/spa")
    public String home() {
        return "spa";
    }

}
