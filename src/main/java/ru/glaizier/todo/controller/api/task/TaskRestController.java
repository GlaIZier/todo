package ru.glaizier.todo.controller.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.glaizier.todo.controller.api.exception.ApiBadRequestException;
import ru.glaizier.todo.controller.api.exception.ApiNotFoundException;
import ru.glaizier.todo.controller.api.exception.ApiTaskNotFoundException;
import ru.glaizier.todo.controller.api.exception.ExceptionHandlingController;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.api.Link;
import ru.glaizier.todo.model.dto.api.output.OutputData;
import ru.glaizier.todo.model.dto.api.output.OutputTask;
import ru.glaizier.todo.persistence.Persistence;
import ru.glaizier.todo.properties.PropertiesService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = {"/api/v1/me/tasks", "/api/me/tasks"})
// Todo create web application
// Todo create react
// Ide shows error but this works
@RequiredArgsConstructor(onConstructor_ = {
        @Autowired
})
public class TaskRestController extends ExceptionHandlingController {

    private static final String TASKS_BASE_URL = "/api/me/tasks/";

    private final Persistence persistence;

    private final PropertiesService propertiesService;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<List<OutputData<OutputTask>>>> getTasks(HttpServletRequest req) {
        String login = getLogin(req);
        List<TaskDto> tasks = persistence.findTasks(login);

        if (tasks == null)
            throw new ApiNotFoundException(format("Tasks list get failed! " +
                    "Login %s hasn't been found!", login));
        if (tasks.isEmpty())
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
    public ResponseEntity<OutputData<OutputTask>> createTask(HttpServletRequest req,
                                                             @RequestParam("todo") String todo) {
        checkTodoIsNotEmpty(todo);
        String login = getLogin(req);

        TaskDto task = persistence.saveTask(login, todo);
        if (task == null)
            throw new ApiNotFoundException(format("Task creation failed! " +
                    "Login %s hasn't been found to create task for!", login));

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = UriComponentsBuilder.newInstance()
                .path(TASKS_BASE_URL)
                .path(String.valueOf(task.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        OutputData<OutputTask> outputData = new OutputData<>(transform(task, login), new Link(locationUri.toString()));
        return new ResponseEntity<>(outputData, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<OutputTask>> getTask(HttpServletRequest req,
                                                          @PathVariable int id) {
        String login = getLogin(req);
        TaskDto task = persistence.findTask(id, login);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);


        OutputData<OutputTask> outputData = new OutputData<>(transform(task, login));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<OutputTask>> updateTask(HttpServletRequest req,
                                                             @PathVariable int id,
                                                             @RequestParam("todo") String todo) {
        String login = getLogin(req);
        TaskDto task = persistence.findTask(id, login);
        if (task == null)
            throw new ApiTaskNotFoundException(login, id);

        TaskDto updatedTask = persistence.updateTask(login, id, todo);

        OutputData<OutputTask> outputData = new OutputData<>(transform(updatedTask, login));
        return new ResponseEntity<>(outputData, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OutputData<OutputTask>> deleteTask(HttpServletRequest req,
                                                             @PathVariable int id) {
        String login = getLogin(req);
        TaskDto deletedTask = persistence.findTask(id, login);
        if (deletedTask == null)
            throw new ApiTaskNotFoundException(login, id);

        persistence.deleteTask(id);

        OutputData<OutputTask> outputData = new OutputData<>(transform(deletedTask, login));
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

    private OutputTask transform(TaskDto taskDto, String login) {
        return OutputTask.builder().id(taskDto.getId()).login(login).todo(taskDto.getTodo()).build();
    }
}