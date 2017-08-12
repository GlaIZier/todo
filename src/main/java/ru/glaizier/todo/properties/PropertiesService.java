package ru.glaizier.todo.properties;

public interface PropertiesService {
    String getApiTokenCookieName();

    String getApiTokenHeaderName();

    int getApiTokenExpireDurationInSeconds();

    String getApiTokenSigningKey();

    String getApiTokenSessionAttributeName();

    String getPasswordEncoderSecret();
}
