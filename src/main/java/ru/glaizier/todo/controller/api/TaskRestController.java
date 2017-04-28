package ru.glaizier.todo.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.api.ApiData;
import ru.glaizier.todo.domain.api.Link;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
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

    //Todo add exception handling for null and other stuff
    // Todo remove hardcode using session and get username from there
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<List<Task>>> getTasks(HttpServletRequest req) {
        List<Task> tasks = taskDao.getTasks("u");
        ApiData<List<Task>> apiData = new ApiData<>(tasks, new Link("http"));
        return new ResponseEntity<>(apiData, null, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiData<Task>> createTask(@RequestBody Task task) {
        task = taskDao.createTask("u", task);
        if (task == null)
            throw new RuntimeException();

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path("/api/me/tasks/")
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"))
        return new ResponseEntity<>(apiData, headers, HttpStatus.CREATED);
    }

    // Todo add here Response Entity
    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiData<Task> getTask(@PathVariable int id) {
        Task task = taskDao.getTask("u", id);
        if (task == null)
            throw new RuntimeException();

        return new ApiData<>(task, new Link("http"));
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiData<Task> updateTask(@PathVariable int id,
                                    @RequestBody Task task) {
        Task updatedTask = new Task(id, "todo" + id);
        if (taskDao.updateTask("u", task) == null)
            throw new RuntimeException();
        return new ApiData<>(updatedTask, new Link("http"));
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiData<Task> deleteTask(Principal principal,
                                    @PathVariable int id) {
        Task task = taskDao.removeTask(principal.getName(), id);
        if (task == null)
            throw new RuntimeException();
        return new ApiData<>(task, new Link("http"));
    }

}