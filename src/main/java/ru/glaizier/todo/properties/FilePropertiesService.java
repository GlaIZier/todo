package ru.glaizier.todo.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Service
@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
public class FilePropertiesService implements PropertiesService {

    @NonNull
    private final String springProfilesActive;
    @NonNull
    private final String springProfilesMemoryName;
    @NonNull
    private final String springProfilesDefaultName;
    @NonNull
    private final String springProfilesProdName;

    @NonNull
    private final String apiTokenCookieName;
    @NonNull
    private final String apiTokenHeaderName;
    private final int apiTokenExpireDurationInSeconds;
    @NonNull
    private final String apiTokenSigningKey;
    @NonNull
    private final String apiTokenSessionAttributeName;

    @NonNull
    private final String passwordEncoderSecret;

    private final String dbUrl;
    private final String dbDriver;
    private final String dbLogin;
    private final String dbPassword;
    private final Integer dbMaxPoolSize;

    // Say this spring to inject values through this constructor, because spring injects through defaults constructor
    // by default
    // Can't inject do field injection because of the lombok's @Builder. It creates constructor but without @Value
    // on the fields
    @Autowired
    private FilePropertiesService(
        @Value("${spring.profiles.active}") String springProfilesActive,
        @Value("${spring.profiles.memory.name}") String springProfilesMemoryName,
        @Value("${spring.profiles.default.name}") String springProfilesDefaultName,
        @Value("${spring.profiles.prod.name}") String springProfilesProdName,
        @Value("${api.token.cookie.name}") String apiTokenCookieName,
        @Value("${api.token.header.name}") String apiTokenHeaderName,
        @Value("${api.token.expire.seconds}") int apiTokenExpireDurationInSeconds,
        @Value("${api.token.signing.key}") String apiTokenSigningKey,
        @Value("${api.token.session.attribute.name}") String apiTokenSessionAttributeName,
        @Value("${password.encoder.secret}") String passwordEncoderSecret,
        @Value("${db.url}") String dbUrl,
        @Value("${db.driver}") String dbDriver,
        @Value("${db.login}") String dbLogin,
        @Value("${db.password}") String dbPassword,
        @Value("${db.maxpoolsize}") Integer dbMaxPoolSize
    ) {
        this.springProfilesActive = springProfilesActive;
        this.springProfilesMemoryName = springProfilesMemoryName;
        this.springProfilesDefaultName = springProfilesDefaultName;
        this.springProfilesProdName = springProfilesProdName;

        this.apiTokenCookieName = apiTokenCookieName;
        this.apiTokenHeaderName = apiTokenHeaderName;
        this.apiTokenExpireDurationInSeconds = apiTokenExpireDurationInSeconds;
        this.apiTokenSigningKey = apiTokenSigningKey;
        this.apiTokenSessionAttributeName = apiTokenSessionAttributeName;

        this.passwordEncoderSecret = passwordEncoderSecret;

        this.dbUrl = dbUrl;
        this.dbDriver = dbDriver;
        this.dbLogin = dbLogin;
        this.dbPassword = dbPassword;
        this.dbMaxPoolSize = dbMaxPoolSize;
    }
}
