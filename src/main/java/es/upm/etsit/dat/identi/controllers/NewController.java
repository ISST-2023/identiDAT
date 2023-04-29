package es.upm.etsit.dat.identi.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.upm.etsit.dat.identi.TokenType;
import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CDToken;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionToken;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartamentRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;

@Controller
@SessionAttributes("censusMemberForm")
public class NewController {

    @Autowired
    private CensusMemberService cenMemService;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private TokenRepository tknRepo;

    @Autowired
    private DelegateRepository dlgRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Autowired
    private DepartamentRepository dpmRepo;

    @Autowired
    private CDTokenRepository cdTknRepo;

    @Autowired
    private CDMemberRepository cdMemRepo;

    @Autowired
    private CommissionRepository cmmRepo;

    @Autowired
    private CommissionTokenRepository cmmTknRepo;

    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/admin/profiles/new")
    public String registerForm( Model model) {
        CensusMemberForm cenMemForm = new CensusMemberForm();
        model.addAttribute("censusMemberForm", cenMemForm);

        List<Degree> degrees = dgrRepo.findAll();
        model.addAttribute("degrees", degrees);
        return "new";
    }

    @PostMapping("/newRegister")
    public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
        
        CensusMemberDto censusMemberDto = null;

        CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());

        if (censusMember != null) {
            model.addAttribute("error",
                    "Este usuario ya existe dentro de la base de datos, " +
                    "si desea modificar alguno de sus atributos por favor intentelo desde la página de profiles.");
            return "error";
        }

        if (censusMember == null) {
            censusMemberDto = new CensusMemberDto(
                    cenMemForm.getName(),
                    cenMemForm.getSurname(),
                    cenMemForm.getEmail(),
                    cenMemForm.getEmail().split("@")[0],
                    cenMemForm.getPersonalID(),
                    cenMemForm.getPhone(),
                    dgrRepo.findByCode(cenMemForm.getDegree()));

            censusMember = modelMapper.map(censusMemberDto, CensusMember.class);
        }

        cenMemRepo.save(censusMember);

        return "redirect:admin/profiles";
    }

}
