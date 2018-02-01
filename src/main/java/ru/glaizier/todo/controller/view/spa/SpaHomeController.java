package ru.glaizier.todo.controller.view.spa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaHomeController {

    // To make spa static resources work we create these three routes to handle /spa, spa/login and spa/tasks/1 routes
    // We can't make it spa/** because it will cover spa/static/** route for spa static content defined in ServletConfig
    @RequestMapping(value = {"/spa", "/spa/*", "/spa/*/*"})
    public String home() {
        return "spa";
    }

}
