package be.ugent.arno.rest.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${spring.security.username}")
    String username;

    @Value("${spring.security.password}")
    String password;

    @Value("${spring.security.roles}")
    String roles;

    @Value("${spring.security.driverClassName}")
    String driverClassName;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(r -> {
                    r.requestMatchers("/h2-console/**").permitAll();
                    r.anyRequest().authenticated();
                })
                .csrf(r -> {
                    r.ignoringRequestMatchers("/h2-console/**");
                    r.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                    r.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
                })
                .headers(r -> r.frameOptions(f -> f.sameOrigin()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(
                User.builder()
                        .passwordEncoder(bCryptPasswordEncoder::encode)
                        .username(username)
                        .password(password)
                        .roles(roles)
                        .build());
        return userDetailsManager;
    }
}
