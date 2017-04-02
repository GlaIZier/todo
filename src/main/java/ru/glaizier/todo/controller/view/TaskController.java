package ru.glaizier.todo.controller.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskController {

    @RequestMapping("/tasks")
    public String tasks() {
        return "tasks";
    }

}
