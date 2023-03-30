package es.upm.etsit.dat.identi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .authorizeHttpRequests((authz) -> authz
                                .requestMatchers("/js", "/css", "/fonts", "/img").permitAll()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(withDefaults());

        return http.build();
    }
}

