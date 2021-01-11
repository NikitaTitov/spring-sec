package ru.nikita.security.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/api/v1/hello")
                .permitAll()
//                .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasAuthority(UserPermissions.WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority(UserPermissions.WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority(UserPermissions.WRITE.getPermission())
                .antMatchers("/api/v1/**").hasAnyRole(UserRoles.ADMIN.name(), UserRoles.OBSERVER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .roles(UserRoles.ADMIN.name())
                .build();

        UserDetails observer = User.builder()
                .username("observer")
                .password(encoder.encode("observer"))
                .roles(UserRoles.OBSERVER.name())
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("user"))
                .roles(UserRoles.USER.name())
                .build();

        return new InMemoryUserDetailsManager(
                admin,
                observer,
                user
        );
    }
}
