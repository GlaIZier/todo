package ru.glaizier.todo.log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {

    @Before("execution(* ru.glaizier.todo.controller.api.exception.ExceptionHandlingController.*(..))")
    public void logError() {
        System.out.println("Hello from aspect!!!");
    }

}
