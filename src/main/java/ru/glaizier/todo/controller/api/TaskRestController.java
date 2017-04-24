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

import java.net.URI;
import java.security.Principal;
import java.util.List;

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
//    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<List<Task>> getTasks(Principal principal) {
//        return new ResponseEntity<>(taskDao.getTasks(principal.getName()), null, HttpStatus.OK);
//    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<List<Task>>> getTasks(Principal principal) {
        return new ResponseEntity<>(new ApiData<>(taskDao.getTasks(principal.getName())), null, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiData<Task>> createTask(Principal principal,
                                                    @RequestBody Task task) {
        task = taskDao.createTask(principal.getName(), task);
        if (task == null)
            throw new RuntimeException();

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path("/api/me/tasks/")
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        return new ResponseEntity<>(new ApiData<>(task), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiData<Task> getTask(Principal principal,
                                 @PathVariable int id) {
        Task task = taskDao.getTask(principal.getName(), id);
        if (task == null)
            throw new RuntimeException();
        return new ApiData<>(task);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiData<Task> updateTask(Principal principal,
                                    @PathVariable int id,
                                    @RequestBody Task task) {
        task.setId(id);
        if (taskDao.updateTask(principal.getName(), task) == null)
            throw new RuntimeException();
        return new ApiData<>(task);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiData<Task> deleteTask(Principal principal,
                                    @PathVariable int id) {
        Task task = taskDao.removeTask(principal.getName(), id);
        if (task == null)
            throw new RuntimeException();
        return new ApiData<>(task);
    }

}