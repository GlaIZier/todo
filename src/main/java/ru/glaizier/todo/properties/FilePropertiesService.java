package ru.glaizier.todo.properties;

import lombok.*;
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

    private final int apiTokenExpireDurationInSeconds;

    @NonNull
    private final String apiTokenSigningKey;

    @NonNull
    private final String apiTokenSessionAttributeName;

    // Say this spring to inject values through this constructor, because spring injects through defaults constructor
    // by default
    // Can't inject do field injection because of the lombok's @Builder. It creates constructor but without @Value
    // on the fields
    @Autowired
    private FilePropertiesService(
            @Value("${api.token.cookie.name}") String apiTokenCookieName,
            @Value("${api.token.expire.seconds}") int apiTokenExpireDurationInSeconds,
            @Value("${api.token.signing.key}") String apiTokenSigningKey,
            @Value("${api.token.session.attribute.name}") String apiTokenSessionAttributeName) {
        this.apiTokenCookieName = apiTokenCookieName;
        this.apiTokenExpireDurationInSeconds = apiTokenExpireDurationInSeconds;
        this.apiTokenSigningKey = apiTokenSigningKey;
        this.apiTokenSessionAttributeName = apiTokenSessionAttributeName;
    }
}
