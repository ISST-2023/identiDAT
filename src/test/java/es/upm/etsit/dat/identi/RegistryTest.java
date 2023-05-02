package es.upm.etsit.dat.identi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CDToken;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionToken;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;

@SpringBootTest(classes = IdentidatApplication.class)
@AutoConfigureMockMvc
public class RegistryTest {

    private MockMvc mockMvc;

    @Autowired
    private RegistryController registryController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CDTokenRepository cdTknRepo;

    @Autowired
    private DepartmentRepository dptRepo;

    @Autowired
    private CommissionTokenRepository cmmTknRepo;

    @Autowired
    private CommissionRepository cmmRepo;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Autowired
    private CDMemberRepository cdMemRepo;

    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Mock
    DefaultOAuth2User principal;

    private static String cdTokenString;
    private static String cmmTokenString;

    private String givenName = "Perico";
    private String familyName = "Pérez Pérez";
    private String username = "p.perez";
    private String email = "p.perez@alumnos.upm.es";
    private String personalID = "73745001D";
    private String phone = "666666666";
    private String degreeCode = "09DA";

    @BeforeAll
    public static void setTokenStrings() {
        cdTokenString = RandomStringUtils.randomAlphanumeric(20);
        cmmTokenString = RandomStringUtils.randomAlphanumeric(20);
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

    @BeforeEach
    public void contextLoads() throws Exception {
        assertThat(registryController).isNotNull();
    }

    @Test
    public void checkRegisterForm() throws Exception {
        CDToken cdToken = new CDToken(cdTokenString, dptRepo.findByAcronym("DIT"));
        cdTknRepo.saveAndFlush(cdToken);

        assertNotNull(cdTknRepo.findByToken(cdTokenString));

        this.mockMvc.perform(get("/register?token=" + cdTokenString)).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Registro en el censo de DAT")));
    }

    @Test
    public void checkFormSubmission() throws Exception {
        CDToken cdToken = new CDToken(cdTokenString, dptRepo.findByAcronym("DIT"));
        cdTknRepo.saveAndFlush(cdToken);

        assertNotNull(cdTknRepo.findByToken(cdTokenString));

        CensusMemberForm cenMemForm = new CensusMemberForm(this.givenName, this.familyName, this.email, this.degreeCode,
                TokenType.CD, cdTokenString);
        cenMemForm.setPersonalID(this.personalID);
        cenMemForm.setPhone(this.phone);
        cenMemForm.setAgreement(true);

        this.mockMvc.perform(post("/register").sessionAttr("censusMemberForm", cenMemForm))
                .andExpect(status().isFound());

        CensusMember censusMember = cenMemRepo.findByUsername(this.username);
        assertEquals(this.givenName, censusMember.getName());
        assertEquals(this.familyName, censusMember.getSurname());
        assertEquals(this.personalID, censusMember.getPersonalID());
        assertEquals(this.username, censusMember.getUsername());
        assertEquals(this.email, censusMember.getEmail());
        assertEquals(this.phone, censusMember.getPhone());
        assertEquals(dgrRepo.findByCode("09DA"), censusMember.getDegree());

        List<CDMember> cdPositions = cdMemRepo.findByCensusMember(censusMember);
        assertEquals(1, cdPositions.size());
        assertEquals(dptRepo.findByAcronym("DIT"), cdPositions.get(0).getDepartment());
    }

    @Test
    public void checkPositionAddition() throws Exception {
        Commission je = cmmRepo.findById(Long.valueOf(1)).get();
        CommissionToken cmmToken = new CommissionToken(cmmTokenString, je);
        cmmTknRepo.saveAndFlush(cmmToken);

        assertNotNull(cmmTknRepo.findByToken(cmmTokenString));

        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(66), this.givenName, this.familyName, this.email,
        this.username, this.personalID, this.phone, dgrRepo.findByCode(this.degreeCode), null, null));

        CensusMemberForm cenMemForm = new CensusMemberForm(this.givenName, this.familyName, this.email, this.degreeCode,
                TokenType.COMMISSION, cmmTokenString);

        this.mockMvc.perform(post("/register").sessionAttr("censusMemberForm", cenMemForm)).andDo(print())
                .andExpect(status().isFound());

        List<CommissionMember> cmmPositions = cmmMemRepo.findByCensusMember(cenMemRepo.findByUsername(this.username));
        assertEquals(1, cmmPositions.size());
        assertEquals(je, cmmPositions.get(0).getCommission());
    }

    @AfterEach
    public void cleanDB() {
        CensusMember censusMember = cenMemRepo.findByUsername(this.username);
        if (censusMember != null) {
            cenMemRepo.delete(censusMember);
            cenMemRepo.flush();
        }

        CDToken cdToken = cdTknRepo.findByToken(cdTokenString);
        if (cdToken != null) {
            cdTknRepo.delete(cdToken);
            cdTknRepo.flush();
        }

        CommissionToken cmmToken = cmmTknRepo.findByToken(cmmTokenString);
        if (cmmToken != null) {
            cmmTknRepo.delete(cmmToken);
            cmmTknRepo.flush();
        }
    }

}