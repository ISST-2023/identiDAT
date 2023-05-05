package es.upm.etsit.dat.identi.configurations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Privilege;
import es.upm.etsit.dat.identi.persistence.model.Role;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {

        @Autowired
        CensusMemberRepository cenMemRepo;

        @Value("${spring.profiles.active}")
        private String activeProfile;

        @Bean
        public SecurityFilterChain configChain(HttpSecurity http, HttpServletRequest httpServletRequest)
                        throws Exception {
                http
                                .oauth2Login(login -> login
                                                .loginPage("/login")
                                                .permitAll()
                                                .userInfoEndpoint()
                                                .userAuthoritiesMapper(this.userAuthoritiesMapper()))
                                .authorizeHttpRequests((authz) -> authz
                                                .requestMatchers("/js", "/css", "/fonts", "/img").permitAll()
                                                .anyRequest().permitAll()
                                                /*.requestMatchers(new AntPathRequestMatcher("/admin/jd/**"))
                                                .hasAuthority("CALL_PRIVILEGE")
                                                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated()*/
                                                );

                return http.build();
        }

        @Bean
        public GrantedAuthoritiesMapper userAuthoritiesMapper() {
                return (authorities) -> {
                        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

                        authorities.forEach(authority -> {
                                if (OAuth2UserAuthority.class.isInstance(authority)) {
                                        OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

                                        Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                                        CensusMember censusMember = cenMemRepo.findByUsername(
                                                        (String) userAttributes.get("preferred_username"));
                                        if (censusMember != null) {
                                                Set<Role> roles = new HashSet<>(censusMember.getRoles());
                                                for (Role role : roles)
                                                        mappedAuthorities.add(
                                                                        new SimpleGrantedAuthority(role.getName()));
                                                                        
                                                Set<Privilege> privileges = new HashSet<>(censusMember.getPrivileges());
                                                for (Privilege privilege : privileges)
                                                        mappedAuthorities.add(
                                                                        new SimpleGrantedAuthority(privilege.getName()));
                                        }
                                }
                        });
                        return mappedAuthorities;
                };
        }

}
