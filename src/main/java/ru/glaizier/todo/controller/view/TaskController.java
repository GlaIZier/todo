package ru.glaizier.todo.controller.view;


import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.persistence.Persistence;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private Persistence persistence;

    @Autowired
    public TaskController(Persistence persistence) {
        this.persistence = persistence;
    }

    @RequestMapping(method = GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    // Todo check why couldn't find session on mac when not incognito window
    public String getTasks(@AuthenticationPrincipal User activeUser, Model model) {
        List<TaskDto> tasks = persistence.findTasks(activeUser.getUsername());
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

}
