package ru.glaizier.todo.controller.view.spa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaHomeController {

    // Todo make comments about this hack
    @RequestMapping(value = {"/spa", "/spa/*", "/spa/*/*"})
    public String home() {
        return "spa";
    }

}
