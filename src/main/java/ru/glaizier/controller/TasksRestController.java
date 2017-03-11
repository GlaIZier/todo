package ru.glaizier.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glaizier.domain.Task;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class TasksRestController {

    @RequestMapping(value = "/tasks", produces = "application/json")
    public List<Task> tasks() {
        return Arrays.asList(new Task("Task1"), new Task("task2"));
    }

}
