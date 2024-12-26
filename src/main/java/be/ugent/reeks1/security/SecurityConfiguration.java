package be.ugent.reeks1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Value;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Value("${security.admin.user}")
    String user;

    @Value("{security.admin.password}")
    String password;

    @Value("{security.admin.roles}")
    String roles;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/blogposts/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher(HttpMethod.PUT, "/blogposts/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher(HttpMethod.DELETE, "/blogposts/**")).hasRole("ADMIN")
                            .anyRequest().permitAll();
                })
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}