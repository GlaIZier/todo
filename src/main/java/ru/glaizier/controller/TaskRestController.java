package ru.glaizier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glaizier.dao.TaskDao;
import ru.glaizier.domain.Task;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = {"/api/v1/", "/api/"})
// todo create rest architecture
// todo create react
// Todo think about the best possible way for rest authentication
// Todo add method security
// Todo add different views for rest (html+json)?
public class TaskRestController {

    // Todo crete TaskService that will hold Dao and TaskLinkCreator Service
    private TaskDao taskDao;

    @Autowired
    public TaskRestController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @RequestMapping(value = "/tasks", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> tasks(Principal principal) {
        return taskDao.getTasks(principal.getName());
    }

}
