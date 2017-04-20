package ru.glaizier.todo.properties;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode
@ToString
@Builder
public class FilePropertiesService implements PropertiesService {

    @Value("${api.token.cookie.name}")
    private String apiTokenCookieName;

    @Value("${api.token.expire.seconds}")
    private int apiTokenExpireDurationInSeconds;

    @Value("${api.token.signing.key}")
    private String apiTokenSigningKey;

    public String getApiTokenCookieName() {
        return this.apiTokenCookieName;
    }

    public int getApiTokenExpireDurationInSeconds() {
        return this.apiTokenExpireDurationInSeconds;
    }

    public String getApiTokenSigningKey() {
        return this.apiTokenSigningKey;
    }

//    private FilePropertiesService(
//            @Value("${api.token.cookie.name}") String apiTokenCookieName,
//            @Value("${api.token.expire.seconds}") int apiTokenExpireDurationInSeconds,
//            @Value("${api.token.signing.key}") String apiTokenSigningKey) {
//        this.apiTokenCookieName = apiTokenCookieName;
//        this.apiTokenExpireDurationInSeconds = apiTokenExpireDurationInSeconds;
//        this.apiTokenSigningKey = apiTokenSigningKey;
//    }

}
