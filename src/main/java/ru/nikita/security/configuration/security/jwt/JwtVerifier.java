package ru.nikita.security.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nikita.security.configuration.ApplicationSettings;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtVerifier extends OncePerRequestFilter {

	private final ApplicationSettings applicationSettings;

	public JwtVerifier(ApplicationSettings applicationSettings) {
		this.applicationSettings = applicationSettings;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");

		if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.replace("Bearer ", "");

		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder()
					.setSigningKey(applicationSettings.getJwtKey())
					.build()
					.parseClaimsJws(token);
			Claims body = claimsJws.getBody();
			String userName = body.getSubject();
			List<Map<String, String>> test = (List<Map<String, String>>) body.get("pusya", List.class);

			List<SimpleGrantedAuthority> authorities = test
					.stream()
					.map(stringStringMap -> new SimpleGrantedAuthority(stringStringMap.get("authority")))
					.collect(Collectors.toList());

			UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(
					userName,
					null,
					authorities
			);
			SecurityContextHolder.getContext().setAuthentication(a);
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		} catch (JwtException e) {
			log.error(e.getLocalizedMessage());
		}

	}
}
