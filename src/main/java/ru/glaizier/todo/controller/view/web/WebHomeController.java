package ru.glaizier.todo.controller.view.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebHomeController {

    @RequestMapping("/web")
    public String home() {
        return "home";
    }

}
