package ru.glaizier.todo.config.servlet;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.glaizier.todo.properties.PropertiesService;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    PropertiesService propertiesService;

    @Autowired
    public SwaggerConfig(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    // Swagger bean
    // http://localhost:8080/todo/swagger-ui.html#/
    // http://localhost:8080/todo/v2/api-docs
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .globalOperationParameters(Collections.singletonList(
                new ParameterBuilder()
                    .name(propertiesService.getApiTokenHeaderName())
                    .description("Api authentication token")
                    .modelRef(new ModelRef("String"))
                    .parameterType("header")
                    .required(false)
                    .build()
            ))
            .select()
            .apis(RequestHandlerSelectors.basePackage("ru.glaizier.todo.controller.api"))
            // get only api paths without version inside
            .paths(PathSelectors.regex("\\/api\\/(?!v\\d*)[\\S\\s]*"))
            .build();
    }

}
