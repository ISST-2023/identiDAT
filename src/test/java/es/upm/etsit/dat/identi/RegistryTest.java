package es.upm.etsit.dat.identi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.upm.etsit.dat.identi.controllers.RegistryController;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistryTest {
    
    private MockMvc mockMvc;

    @Autowired
    private RegistryController registryController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenRepository tknRepo;

    @Mock
    DefaultOAuth2User principal;

    @Test
    public void contextLoads() throws Exception {
        assertThat(registryController).isNotNull();
    }

    @BeforeEach
    public void beforeEach() {
        Authentication authentication = mock(OAuth2AuthenticationToken.class);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        when(principal.getAttribute("email")).thenReturn("p.perez@alumnos.upm.es");
        when(principal.getAttribute("given_name")).thenReturn("Perico");
        when(principal.getAttribute("family_name")).thenReturn("Pérez Pérez");
    }

    @Test
    public void checkRegisterForm() throws Exception {
        String token = tknRepo.findAll().get(0).getToken();
        this.mockMvc.perform(get("/register?token=" + token)).andDo(print()).andExpect(status().isOk());
    }
}