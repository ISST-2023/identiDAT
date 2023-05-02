package es.upm.etsit.dat.identi.admin;

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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.upm.etsit.dat.identi.IdentidatApplication;
import es.upm.etsit.dat.identi.controllers.admin.ProfilesController;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;

@SpringBootTest(classes = IdentidatApplication.class)
@AutoConfigureMockMvc
public class CensusTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProfilesController profilesController;

    @Mock
    DefaultOAuth2User principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DepartmentRepository dptRepo;

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

    private String givenName = "Perico";
    private String familyName = "Pérez Pérez";
    private String username = "p.perez";
    private String email = "p.perez@alumnos.upm.es";
    private String personalID = "73745001D";
    private String phone = "666666666";
    private String degreeCode = "09DA";

    @BeforeEach
    public void contextLoads() throws Exception {
        assertThat(profilesController).isNotNull();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void checkNewForm() throws Exception {
        this.mockMvc.perform(get("/admin/census/new")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Registro en el censo de DAT")));
    }

    @Test
    public void checkNewFormSubmission() throws Exception {
        CensusMemberForm cenMemForm = new CensusMemberForm(this.givenName, this.familyName, this.email, this.degreeCode,
                null, null);
        cenMemForm.setPersonalID(this.personalID);
        cenMemForm.setPhone(this.phone);
        cenMemForm.setAgreement(true);

        this.mockMvc.perform(post("/admin/census/new").flashAttr("censusMemberForm", cenMemForm))
                .andExpect(status().isFound());

        CensusMember censusMember = cenMemRepo.findByUsername(this.username);
        assertEquals(this.givenName, censusMember.getName());
        assertEquals(this.familyName, censusMember.getSurname());
        assertEquals(this.personalID, censusMember.getPersonalID());
        assertEquals(this.username, censusMember.getUsername());
        assertEquals(this.email, censusMember.getEmail());
        assertEquals(this.phone, censusMember.getPhone());
        assertEquals(dgrRepo.findByCode("09DA"), censusMember.getDegree());
    }

    @Test
    public void checkEditForm() throws Exception {
        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(66), this.givenName, this.familyName, this.email,
                this.username, this.personalID, this.phone, dgrRepo.findByCode(this.degreeCode), null, null));

        CensusMember censusMember = cenMemRepo.findByUsername(this.username);

        this.mockMvc.perform(get("/admin/census/edit/" + censusMember.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("identiDAT - Editar Usuario")));
    }

    @Test
    public void checkEditFormSubmission() throws Exception {
        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(66), this.givenName, this.familyName, this.email,
                this.username, this.personalID, this.phone, dgrRepo.findByCode(this.degreeCode), null, null));

        CensusMember censusMember = cenMemRepo.findByUsername(this.username);

        CensusMemberForm cenMemForm = new CensusMemberForm(this.givenName, this.familyName, "perico.perez@alumnos.upm.es", "09TT",
                null, null);
        cenMemForm.setCensusMemberId(censusMember.getId());
        cenMemForm.setPersonalID(this.personalID);
        cenMemForm.setPhone("666666667");

        this.mockMvc.perform(post("/admin/census/edit").flashAttr("censusMemberForm", cenMemForm))
                .andExpect(status().isFound());

        censusMember = cenMemRepo.findByUsername(this.username);
        assertEquals(this.givenName, censusMember.getName());
        assertEquals(this.familyName, censusMember.getSurname());
        assertEquals(this.personalID, censusMember.getPersonalID());
        assertEquals(this.username, censusMember.getUsername());
        assertEquals("perico.perez@alumnos.upm.es", censusMember.getEmail());
        assertEquals("666666667", censusMember.getPhone());
        assertEquals(dgrRepo.findByCode("09TT"), censusMember.getDegree());
    }

    @Disabled("Aún no está implementado.")
    @Test
    public void checkPositionAddition() throws Exception {
        Commission je = cmmRepo.findById(Long.valueOf(1)).get();

        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(66), this.givenName, this.familyName, this.email,
                this.username, this.personalID, this.phone, dgrRepo.findByCode(this.degreeCode), null, null));

        CensusMemberForm cenMemForm = new CensusMemberForm(this.givenName, this.familyName, this.email, this.degreeCode,
                null, null);

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
    }

}