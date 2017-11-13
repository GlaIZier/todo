package ru.glaizier.todo.properties;

public interface PropertiesService {
    // Spring profile
    String getSpringProfilesActive();

    String getSpringProfilesMemoryName();

    String getSpringProfilesDefaultName();

    String getSpringProfilesProdName();

    // Api
    String getApiTokenCookieName();
    String getApiTokenHeaderName();
    int getApiTokenExpireDurationInSeconds();
    String getApiTokenSigningKey();
    String getApiTokenSessionAttributeName();

    // Secrets
    String getPasswordEncoderSecret();

    // Db connection
    String getDbUrl();
    String getDbDriver();
    String getDbLogin();
    String getDbPassword();
    Integer getDbMaxPoolSize();
}
