package ru.glaizier.todo.controller.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.glaizier.todo.controller.api.exception.ApiBadRequestException;
import ru.glaizier.todo.controller.api.exception.ApiNotFoundException;
import ru.glaizier.todo.controller.api.exception.ApiTaskNotFoundException;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.api.Link;
import ru.glaizier.todo.domain.api.output.OutputData;
import ru.glaizier.todo.properties.PropertiesService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
// todo create react
// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
// Todo add AuthRestController to authenticate user via rest
// Todo consume all in json? not in xxx form url encoded

// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class TaskRestController extends ExceptionHandlingController {

    // Todo crete TaskService that will hold Dao and TaskLinkCreator (or autogenerate this instead) Service
    private final TaskDao taskDao;

    private final PropertiesService propertiesService;

    /**
     * Methods
     */
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Set<Task>>> getTasks(HttpServletRequest req) {
        Set<Task> tasks = taskDao.getTasks(getLogin(req));
        OutputData<Set<Task>> outputData = new OutputData<>(tasks, new Link("http"));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            /*consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}*/)
    public ResponseEntity<OutputData<Task>> createTask(HttpServletRequest req,
                                                       @RequestBody String todo) {
        checkTodoIsNotEmpty(todo);
        String login = getLogin(req);
        Task task = taskDao.createTask(login, todo);
        if (task == null)
            throw new ApiNotFoundException(format("Task creation failed! " +
                    "Login %s hasn't been found to create task for!", login));

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path("/api/me/tasks/")
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        OutputData<Task> outputData = new OutputData<>(task, new Link("http"));
        return new ResponseEntity<>(outputData, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> getTask(HttpServletRequest req,
                                                    @PathVariable int id) {
        String login = getLogin(req);
        Task task = taskDao.getTask(login, id);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);

        OutputData<Task> outputData = new OutputData<>(task, new Link("http"));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            /*consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}*/)
    public ResponseEntity<OutputData<Task>> updateTask(HttpServletRequest req,
                                                       @PathVariable int id,
                                                       @RequestBody String todo) {
        Task updatedTask = new Task(id, todo);
        String login = getLogin(req);
        if (taskDao.updateTask(login, updatedTask) == null)
            throw new ApiTaskNotFoundException(login, id);

        OutputData<Task> outputData = new OutputData<>(updatedTask, new Link("http"));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> deleteTask(HttpServletRequest req,
                                                       @PathVariable int id) {
        String login = getLogin(req);
        Task task = taskDao.removeTask(login, id);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);

        OutputData<Task> outputData = new OutputData<>(task, new Link("http"));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    private String getLogin(HttpServletRequest req) {
        String login = (String) req.getSession().getAttribute(propertiesService.getApiTokenSessionAttributeName());
        if (login == null)
            throw new ApiBadRequestException("Couldn't get login from Http session!");
        return login;
    }

    private void checkTodoIsNotEmpty(String todo) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(todo)))
            throw new ApiBadRequestException("Provided todo is empty or null!");
    }

}