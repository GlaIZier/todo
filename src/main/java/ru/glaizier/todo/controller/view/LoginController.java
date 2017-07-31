package ru.glaizier.todo.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        HttpServletRequest httpServletRequest,
                        Model model) {
        if (httpServletRequest.getUserPrincipal() != null)
            return "redirect:/";
        httpServletRequest.getSession().setAttribute("requested-uri", httpServletRequest.getRequestURI());
        if (error != null)
            model.addAttribute("error", "Invalid login or password");
        return "login";
    }

}