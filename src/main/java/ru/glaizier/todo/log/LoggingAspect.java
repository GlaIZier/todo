package ru.glaizier.todo.log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Before("execution(* ru.glaizier.todo.controller.api.exception.ExceptionHandlingController.*(..)) && args(e, ..)")
    public void logError(Exception e) {
        log.error("Request to rest controller failed: " + e.getMessage(), e);
    }

}
