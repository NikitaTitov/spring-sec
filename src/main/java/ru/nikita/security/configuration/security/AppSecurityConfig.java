package ru.nikita.security.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder encoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/", "/api/v1/hello")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
					.loginPage("/login")
					.permitAll()
					.defaultSuccessUrl("/courses", true)
					.passwordParameter("customPasswordVariableName")
					.usernameParameter("customUserVariableName")
				.and()
				.rememberMe()
					.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
					.key("KeyThatWillBeUsedWhenWillHashingUsernameAndExpirationTime")
				.and()
				.logout()
					.logoutUrl("logoutCustomUrl")
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID", "remember-me")
					.logoutSuccessUrl("/login");
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		UserDetails admin = User.builder()
				.username("admin")
				.password(encoder.encode("admin"))
				.authorities(UserRoles.ADMIN.getAuthorities())
				.build();

		UserDetails observer = User.builder()
				.username("observer")
				.password(encoder.encode("observer"))
				.authorities(UserRoles.OBSERVER.getAuthorities())
				.build();

		UserDetails user = User.builder()
				.username("user")
				.password(encoder.encode("user"))
				.authorities(UserRoles.USER.getAuthorities())
				.build();

		return new InMemoryUserDetailsManager(
				admin,
				observer,
				user
		);
	}
}
