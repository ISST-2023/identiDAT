package es.upm.etsit.dat.identi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CDToken;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartamentRepository;

@SpringBootTest(classes = IdentidatApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistryTest {

    private MockMvc mockMvc;

    @Autowired
    private RegistryController registryController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CDTokenRepository cdTknRepo;

    @Autowired
    private DepartamentRepository dptRepo;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Mock
    DefaultOAuth2User principal;

    private static String tokenString = "0R4zxtyA6Heip1OhU0bu";
    private static CDToken token;

    @BeforeAll
    public void createToken() throws Exception {
        token = new CDToken(tokenString, dptRepo.findByAcronym("DIT"));
        cdTknRepo.saveAndFlush(token);
    }

    @BeforeEach
    public void generatePrincipal() {
        Authentication authentication = mock(OAuth2AuthenticationToken.class);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        when(principal.getName()).thenReturn("p.perez");
        when(principal.getAttribute("email")).thenReturn("p.perez@alumnos.upm.es");
        when(principal.getAttribute("given_name")).thenReturn("Perico");
        when(principal.getAttribute("family_name")).thenReturn("Pérez Pérez");
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(registryController).isNotNull();
    }

    @Test
    public void checkRegisterForm() throws Exception {
        this.mockMvc.perform(get("/register?token=" + tokenString)).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Registro en el censo de DAT")));
    }

    @Test
    public void checkFormSubmission() throws Exception {
        CensusMemberForm cenMemForm = new CensusMemberForm("Perico", "Pérez Pérez", "p.perez@alumnos.upm.es", "09DA",
                TokenType.CD, tokenString);
        cenMemForm.setPersonalID("73745001D");
        cenMemForm.setPhone("666666666");
        cenMemForm.setAgreement(true);
        this.mockMvc.perform(post("/register").sessionAttr("censusMemberForm", cenMemForm))
                .andExpect(status().isFound());
    }

    @AfterAll
    public void removeToken() throws Exception {
        cdTknRepo.delete(token);
        cenMemRepo.delete(cenMemRepo.findByUsername("p.perez"));
    }
}