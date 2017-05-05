package ru.glaizier.todo.controller.api.task;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.api.ApiData;
import ru.glaizier.todo.domain.api.ApiError;
import ru.glaizier.todo.domain.api.Error;
import ru.glaizier.todo.domain.api.Link;
import ru.glaizier.todo.properties.PropertiesService;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
// todo create react
// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
// Todo add tests for rest controller
// Todo add AuthRestController to authenticate user via rest

// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class TaskRestController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Todo crete TaskService that will hold Dao and TaskLinkCreator (or autogenerate this instead) Service
    private final TaskDao taskDao;

    private final PropertiesService propertiesService;

    // Todo add mdc and logging aspects here to handle exceptionHandlers?
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(
            Exception e) {
        log.error("Request to task rest controller failed with unexpected error: " + e.getMessage(), e);

        ApiError apiError = new ApiError(new Error(ApiError.INTERNAL_SERVER_ERROR.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestControllerBadRequesException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            RestControllerBadRequesException e) {
        log.error("Request to task rest controller failed: " + e.getMessage(), e);

        ApiError apiError = new ApiError(new Error(ApiError.BAD_REQUEST.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestControllerNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(
            RestControllerNotFoundException e) {
        log.error("Request to task rest controller failed: " + e.getMessage(), e);

        ApiError apiError = new ApiError(new Error(ApiError.NOT_FOUND.getError().getCode(), e.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

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
        // Todo
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
        String login = getLogin(req);
        Task task = taskDao.getTask(login, id);
        if (task == null)
            throw new RestControllerNotFoundException(login, id);

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiData<Task>> updateTask(HttpServletRequest req,
                                                    @PathVariable int id,
                                                    @RequestBody Task task) {
        Task updatedTask = new Task(id, "todo" + id);
        String login = getLogin(req);
        if (taskDao.updateTask(login, task) == null)
            throw new RestControllerNotFoundException(login, id);

        ApiData<Task> apiData = new ApiData<>(updatedTask, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApiData<Task>> deleteTask(HttpServletRequest req,
                                                    @PathVariable int id) {
        String login = getLogin(req);
        Task task = taskDao.removeTask(login, id);
        if (task == null)
            throw new RestControllerNotFoundException(login, id);

        ApiData<Task> apiData = new ApiData<>(task, new Link("http"));
        return new ResponseEntity<>(apiData, HttpStatus.OK);
    }

    private String getLogin(HttpServletRequest req) {
        String login = (String) req.getSession().getAttribute(propertiesService.getApiTokenSessionAttributeName());
        if (login == null)
            throw new RestControllerBadRequesException("Couldn't get login from Http session!");
        return login;
    }

}