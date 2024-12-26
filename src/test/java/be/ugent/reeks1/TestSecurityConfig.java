package be.ugent.reeks1;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

import be.ugent.reeks1.security.SecurityConfiguration;

@Profile("dev")
@TestConfiguration
public class TestSecurityConfig extends SecurityConfiguration {
    public CsrfConfigurer<HttpSecurity> csrfSecurity(CsrfConfigurer<HttpSecurity> authorize) {
        System.out.println("csrf: disabled");
        return authorize.ignoringRequestMatchers("/**");
    };
}
