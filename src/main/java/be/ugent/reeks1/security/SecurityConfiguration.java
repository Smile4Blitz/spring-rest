package be.ugent.reeks1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import be.ugent.reeks1.Repository.UserRepository;
import be.ugent.reeks1.models.BlogPostUser;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@Profile("prod")
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${spring.security.user.name}")
    String username;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            BlogPostUser user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(t -> csrfSecurity(t))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> {
                    h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(antMatcher("/csrf/**")).permitAll()
                            .requestMatchers(antMatcher("/error/**")).permitAll()
                            .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/blogposts/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/blogposts/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher(HttpMethod.PUT, "/blogposts/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher(HttpMethod.DELETE, "/blogposts/**")).hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    public CsrfConfigurer<HttpSecurity> csrfSecurity(CsrfConfigurer<HttpSecurity> authorize) {
        System.out.println("csrf: enabled");
        return authorize
                .ignoringRequestMatchers("/csrf/**")
                .ignoringRequestMatchers("/h2-console/**")
                .ignoringRequestMatchers("/error/**");
    };
}