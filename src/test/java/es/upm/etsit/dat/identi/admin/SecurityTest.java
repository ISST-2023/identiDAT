package es.upm.etsit.dat.identi.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.upm.etsit.dat.identi.controllers.BasicViewCotroller;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasicViewCotroller viewMockupController;

    @Mock
    DefaultOAuth2User principal;

    @Test
    public void contextLoads() throws Exception {
        assertThat(viewMockupController).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void checkAdminAccess() throws Exception {
        this.mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "CALL_PRIVILEGE")
    public void checkJDAccess() throws Exception {
        this.mockMvc.perform(get("/admin/jd")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    public void checkUnathorized() throws Exception {
        this.mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void checkAdminUnathorized() throws Exception {
        this.mockMvc.perform(get("/admin/jd")).andExpect(status().isForbidden());
    }
}