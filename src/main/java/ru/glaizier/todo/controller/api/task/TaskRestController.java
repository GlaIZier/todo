package ru.glaizier.todo.controller.api.task;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
import ru.glaizier.todo.dao.Dao;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.User;
import ru.glaizier.todo.domain.api.Link;
import ru.glaizier.todo.domain.api.output.OutputData;
import ru.glaizier.todo.domain.api.output.OutputTask;
import ru.glaizier.todo.properties.PropertiesService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
// todo create react
// Todo add method security
// Todo add different views for rest (html+json)?
// Todo add swagger for rest api
// Todo consume all in json? not in xxx form url encoded
// Todo maybe create controller to get all links for tasks

// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class TaskRestController extends ExceptionHandlingController {

    private static final String TASKS_BASE_URL = "/api/me/tasks/";

    private final Dao dao;

    private final PropertiesService propertiesService;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<List<OutputData<OutputTask>>>> getTasks(HttpServletRequest req) {
        String login = getLogin(req);
        User user = dao.getUserDao().findUserByLogin(login);
        if (user == null)
            throw new ApiNotFoundException(format("Tasks list get failed! " +
                    "Login %s hasn't been found!", login));

        List<Task> tasks = dao.getTaskDao().findTasksByUser(user);
        if (tasks == null)
            return new ResponseEntity<>(new OutputData<>(null), HttpStatus.OK);

        List<OutputTask> outputTasks = tasks.stream().collect(
                ArrayList<OutputTask>::new,
                (acc, task) -> acc.add(OutputTask.builder().id(task.getId()).login(login).todo(task.getTodo()).build()),
                ArrayList::addAll);

        List<OutputData<OutputTask>> outputData = outputTasks.stream().collect(
                ArrayList::new,
                (acc, task) -> acc.add(new OutputData<OutputTask>(task, new Link(TASKS_BASE_URL + task.getId()))),
                ArrayList::addAll);

        return new ResponseEntity<>(new OutputData<>(outputData), HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> createTask(HttpServletRequest req,
                                                       @RequestBody String todo) {
        checkTodoIsNotEmpty(todo);
        String login = getLogin(req);
        User user = dao.getUserDao().findUserByLogin(login);
        if (user == null)
            throw new ApiNotFoundException(format("Task creation failed! " +
                    "Login %s hasn't been found to create task for!", login));

        Task task = dao.getTaskDao().save(Task.builder().user(user).todo(todo).build());

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path(TASKS_BASE_URL)
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        OutputData<Task> outputData = new OutputData<>(task, new Link(locationUri.toString()));
        return new ResponseEntity<>(outputData, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> getTask(HttpServletRequest req,
                                                    @PathVariable int id) {
        String login = getLogin(req);
        Task task = dao.findTaskUserJoinedWithLoginCheck(id, login);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);

        OutputData<Task> outputData = new OutputData<>(task);
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> updateTask(HttpServletRequest req,
                                                       @PathVariable int id,
                                                       @RequestBody String todo) {
        String login = getLogin(req);
        Task task = dao.findTaskUserJoinedWithLoginCheck(id, login);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);

        Task updatedTask = dao.getTaskDao().save(Task.builder().id(id).user(task.getUser()).todo(todo).build());

        OutputData<Task> outputData = new OutputData<>(updatedTask);
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<Task>> deleteTask(HttpServletRequest req,
                                                       @PathVariable int id) {
        String login = getLogin(req);
        Task deletedTask = dao.findTaskUserJoinedWithLoginCheck(id, login);
        if (deletedTask == null)
            throw new ApiTaskNotFoundException(login, id);

        dao.getTaskDao().delete(id);

        OutputData<Task> outputData = new OutputData<>(deletedTask);
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