package ru.glaizier.todo.properties;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
public class FilePropertiesService implements PropertiesService {

    @NonNull
    private final String apiTokenCookieName;

    @NonNull
    private final int apiTokenExpireDurationInSeconds;

    @NonNull
    private final String apiTokenSigningKey;

    // Say this spring to inject values through this constructor
    @Autowired
    private FilePropertiesService(
            @Value("${api.token.cookie.name}") String apiTokenCookieName,
            @Value("${api.token.expire.seconds}") int apiTokenExpireDurationInSeconds,
            @Value("${api.token.signing.key}") String apiTokenSigningKey) {
        this.apiTokenCookieName = apiTokenCookieName;
        this.apiTokenExpireDurationInSeconds = apiTokenExpireDurationInSeconds;
        this.apiTokenSigningKey = apiTokenSigningKey;
    }
}
