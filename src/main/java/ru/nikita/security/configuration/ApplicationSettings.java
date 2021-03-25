package ru.nikita.security.configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationSettings {
	private SecretKey jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	private String version;
}
