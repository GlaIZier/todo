package ru.glaizier.todo.controller.api;

import lombok.RequiredArgsConstructor;
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
import ru.glaizier.todo.properties.PropertiesService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
// todo create rest architecture
// todo create react
// Todo think about the best possible way for rest authentication
// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add exception handling for null and other stuff
// Todo add swagger for rest api
// Todo add tests for rest controller

// ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskRestController {

    // Todo crete TaskService that will hold Dao and TaskLinkCreator (or autogenerate this instead) Service
    private final TaskDao taskDao;

    private final PropertiesService propertiesService;

//    @Autowired
//    public TaskRestController(TaskDao taskDao,
//                              PropertiesService propertiesService) {
//        this.taskDao = taskDao;
//    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<List<Task>>> getTasks(HttpServletRequest req) {
        List<Task> tasks = taskDao.getTasks(getLogin(req));
        ApiData<List<Task>> apiData = new ApiData<>(tasks, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiData<Task>> createTask(HttpServletRequest req,
                                                    @RequestBody Task task) {
        task = taskDao.createTask(getLogin(req), task);
        if (task == null)
            throw new RuntimeException();

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path("/api/me/tasks/")
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"));
        return new ResponseEntity<>(apiData, headers, HttpStatus.CREATED);
    }

    // Todo add here Response Entity
    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<Task>> getTask(HttpServletRequest req,
                                                 @PathVariable int id) {
        Task task = taskDao.getTask(getLogin(req), id);
        if (task == null)
            throw new RestControllerNotFoundException("Task with id " + id + " hasn't been found!");

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiData<Task>> updateTask(HttpServletRequest req,
                                                    @PathVariable int id,
                                                    @RequestBody Task task) {
        Task updatedTask = new Task(id, "todo" + id);
        if (taskDao.updateTask(getLogin(req), task) == null)
            throw new RestControllerNotFoundException("Task with id " + id + " hasn't been found!");

        ApiData<Task> apiData = new ApiData<>(updatedTask, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<Task>> deleteTask(HttpServletRequest req,
                                                    @PathVariable int id) {
        Task task = taskDao.removeTask(getLogin(req), id);
        if (task == null)
            throw new RestControllerNotFoundException("Task with id " + id + " hasn't been found!");

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    private String getLogin(HttpServletRequest req) {
        String login = (String) req.getSession().getAttribute(propertiesService.getApiTokenSessionAttributeName());
        if (login == null)
            throw new RestControllerException("Couldn't get login from Http session!");
        return login;
    }

}