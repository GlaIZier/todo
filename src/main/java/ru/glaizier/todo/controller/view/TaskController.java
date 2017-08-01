package ru.glaizier.todo.controller.view;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskController {

    @RequestMapping("/tasks")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String tasks() {
        return "tasks";
    }

}
