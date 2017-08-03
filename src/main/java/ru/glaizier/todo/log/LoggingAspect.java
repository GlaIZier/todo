package ru.glaizier.todo.log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* ru.glaizier.todo.controller.api.exception.ExceptionHandlingController.*(..))")
    public void logError() {
        System.out.println("Hello from aspect!!!");
    }

    @Before("execution(* ru.glaizier.todo.controller.api.task.TaskRestController.*(..))")
    public void logTask() {
        System.out.println("Hello from aspect task!!!");
    }

}
