package ru.glaizier.todo.properties;

public interface PropertiesService {
    String getApiTokenCookieName();

    int getApiTokenExpireDurationInSeconds();

    String getApiTokenSigningKey();
}
