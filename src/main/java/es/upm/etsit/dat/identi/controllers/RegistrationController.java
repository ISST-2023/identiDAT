package es.upm.etsit.dat.identi.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;

@Controller
@SessionAttributes("censusMemberForm")
public class RegistrationController {

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private TokenRepository tknRepo;

    @Autowired
    private DelegateRepository dlgRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Autowired
    private CDTokenRepository cdTknRepo;

    @Autowired
    private CDMemberRepository cdMemRepo;

    @Autowired
    private CommissionTokenRepository cmmTknRepo;

    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/register")
    @SuppressWarnings("null")
    public String registerForm(@AuthenticationPrincipal OAuth2User principal,
            @RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null) {
            model.addAttribute("error",
                    "No dispones de un token válido. Contacta con un administrador para continuar.");
            return "error";
        }

        TokenType tokenType;

        Token positionToken = tknRepo.findByToken(token);
        CDToken cdToken = cdTknRepo.findByToken(token);
        CommissionToken commissionToken = cmmTknRepo.findByToken(token);

        if (positionToken != null)
            tokenType = TokenType.POSITION;
        else if (cdToken != null)
            tokenType = TokenType.CD;
        else if (commissionToken != null)
            tokenType = TokenType.COMMISSION;
        else {
            model.addAttribute("error",
                    "No dispones de un token válido. Contacta con un administrador para continuar.");
            return "error";
        }

        String email;
        String given_name;
        String family_name;
        try {
            email = principal.getAttribute("email");
            given_name = principal.getAttribute("given_name");
            family_name = principal.getAttribute("family_name");
        } catch (Exception e) {
            model.addAttribute("error",
                    "No se han podido recuperar los datos del usuario. Inicia sesión e inténtalo de nuevo.");
            return "error";
        }

        String degree = principal.getAttribute("degree") == null ? "DAT" : principal.getAttribute("degree");

        CensusMemberForm cenMemForm = new CensusMemberForm(given_name, family_name, email,
                degree, tokenType, token);
        model.addAttribute("censusMemberForm", cenMemForm);

        if (cenMemRepo.findByEmail(email) != null) {
            cenMemForm.setAgreement(true);
            return register(cenMemForm, model);
        }

        switch (tokenType) {
            case POSITION:
                model.addAttribute("position", positionToken.getPosition().getName()); 
                model.addAttribute("diferentiator", positionToken.getDiferentiator());
                break;
            case CD:
                model.addAttribute("position", "Consejo de Departamento " + cdToken.getDepartment().getAcronym());
                break;
            case COMMISSION:
                model.addAttribute("position", commissionToken.getCommission().getName());
                break;
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
        if (!cenMemForm.getAgreement()) {
            model.addAttribute("error", "No se ha aceptado el acuerdo de usuario.");
            return "error";
        }

        CensusMemberDto censusMemberDto = null;
        TokenType tokenType = cenMemForm.getTokenType();

        CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());

        if (censusMember == null) {
            OAuth2User principal = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            censusMemberDto = new CensusMemberDto(
                    cenMemForm.getName(),
                    cenMemForm.getSurname(),
                    cenMemForm.getEmail(),
                    principal.getName(),
                    cenMemForm.getPersonalID(),
                    cenMemForm.getPhone(),
                    dgrRepo.findByAcronym(cenMemForm.getDegree()));

            censusMember = modelMapper.map(censusMemberDto, CensusMember.class);
        }

        switch (tokenType) {
            case POSITION:
                Token token = tknRepo.findByToken(cenMemForm.getToken());
                if (token == null) {
                    model.addAttribute("error",
                            "No dispones de un token válido. Contacta con un administrador para continuar.");
                    return "error";
                }
                Position position = token.getPosition();
                Degree degree = token.getDegree();
                Integer diferentiator = token.getDiferentiator();
                if (dlgRepo.findByPositionAndDiferentiatorAndYear(position, diferentiator, 2023) != null) {
                    model.addAttribute("error", "Este cargo ya está registrado para este curso académico.");
                    return "error";
                }
                if (censusMemberDto != null)
                    censusMember = modelMapper.map(censusMemberDto, CensusMember.class);
                Delegate memberPosition = new Delegate(censusMember, position, degree, diferentiator, 2023);
                dlgRepo.save(memberPosition);
                break;

            case CD:
                CDToken cdToken = cdTknRepo.findByToken(cenMemForm.getToken());
                if (cdToken == null) {
                    model.addAttribute("error",
                            "No dispones de un token válido. Contacta con un administrador para continuar.");
                    return "error";
                }
                CDMember cdMember = new CDMember(censusMember, cdToken.getDepartment(), 2023, false);
                cdMemRepo.save(cdMember);
                break;

            case COMMISSION:
                CommissionToken cmmToken = cmmTknRepo.findByToken(cenMemForm.getToken());
                if (cmmToken == null) {
                    model.addAttribute("error",
                            "No dispones de un token válido. Contacta con un administrador para continuar.");
                    return "error";
                }
                CommissionMember cmmMember = new CommissionMember(censusMember, cmmToken.getCommission(), 2023);
                cmmMemRepo.save(cmmMember);
                break;
        }

        cenMemRepo.save(censusMember);

        return "redirect:profile";
    }
}
