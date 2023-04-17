package es.upm.etsit.dat.identi.configurations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import es.upm.etsit.dat.identi.handlers.SessionInterceptor;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;

@Configuration
public class IntercepetorConfigurator implements WebMvcConfigurer {
    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludedPatterns = new ArrayList<String> ();
        excludedPatterns.add("/js/**");
        excludedPatterns.add("/css/**");
        excludedPatterns.add("/img/**");
        excludedPatterns.add("/login");
        excludedPatterns.add("/favicon.svg");
        registry.addInterceptor(new SessionInterceptor(cenMemRepo, activeProfile))
            .addPathPatterns("/**")
            .excludePathPatterns(excludedPatterns);
            
    }
}
