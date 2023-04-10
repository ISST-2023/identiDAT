package es.upm.etsit.dat.identi.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configChain(HttpSecurity http, HttpServletRequest httpServletRequest) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/js", "/css", "/fonts", "/img").permitAll()
                    .anyRequest().authenticated()                              
                )
                .oauth2Login(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/loginSuccess", true)
                    .permitAll()
                );

        return http.build();
    }
}

