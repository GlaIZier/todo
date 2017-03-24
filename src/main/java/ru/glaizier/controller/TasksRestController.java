package ru.glaizier.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glaizier.domain.Task;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
// todo create rest architecture
// todo create react
// Todo think about the best possible way for rest authentication
public class TasksRestController {

    @RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> tasks(Principal principal) {
        return Arrays.asList(new Task(principal.getName() + "1"), new Task(principal.getName() + "2"));
    }

}
