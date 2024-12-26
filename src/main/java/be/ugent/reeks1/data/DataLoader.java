package be.ugent.reeks1.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import be.ugent.reeks1.Repository.UserRepository;
import be.ugent.reeks1.models.BlogPostUser;

@Component
public class DataLoader {
    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    @Value("${spring.security.user.roles}")
    String roles;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername(username) == null) {
                BlogPostUser user = new BlogPostUser();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setRoles(roles);
                userRepository.save(user);
            }
        };
    }
}
