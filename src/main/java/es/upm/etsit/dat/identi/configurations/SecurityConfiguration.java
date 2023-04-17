package es.upm.etsit.dat.identi.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain configChain(HttpSecurity http, HttpServletRequest httpServletRequest) throws Exception {
        System.out.println("Perfil activo: " + activeProfile);
        if (!activeProfile.equals("dev")) {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/js", "/css", "/fonts", "/img").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(login -> login
                        .loginPage("/login")
                        .permitAll());
        } else {
            http
            .authorizeHttpRequests((authz) -> authz
                    .anyRequest().permitAll())
            .oauth2Login(login -> login
                    .loginPage("/login")
                    .permitAll());
        }

        return http.build();
    }
}
