package ru.nikita.security.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nikita.security.configuration.ApplicationSettings;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final ApplicationSettings applicationSettings;

	public JwtAuthFilter(AuthenticationManager authenticationManager, ApplicationSettings applicationSettings) {
		this.authenticationManager = authenticationManager;
		this.applicationSettings = applicationSettings;
		this.setUsernameParameter("someOtherTestUsername");
		this.setPasswordParameter("customPassword");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = this.obtainUsername(request);
		String password = this.obtainPassword(request);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				username,
				password
		);

		Authentication authenticate = authenticationManager.authenticate(authentication);

		return authenticate;
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult
	) throws IOException, ServletException {

		String token = Jwts.builder()
				.setSubject(authResult.getName())
				.claim("pusya", authResult.getAuthorities())
				.setIssuedAt(new Date())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1L)))
				.signWith(applicationSettings.getJwtKey())
				.compact();

		response.addHeader("Authorization", "Bearer " + token);

	}
}
