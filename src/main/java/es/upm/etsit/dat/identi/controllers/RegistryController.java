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

import es.upm.etsit.dat.identi.FieldValidator;
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
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;

@Controller
@SessionAttributes("censusMemberForm")
public class RegistryController {

    @Autowired
    private SettingRepository stngRepo;

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

    @Autowired
    private FieldValidator fieldValidator;

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

        String degreeCode = principal.getAttribute("degree") == null ? "09DA" : principal.getAttribute("degree");

        CensusMemberForm cenMemForm = new CensusMemberForm(given_name, family_name, email,
                degreeCode, tokenType, token);

        cenMemForm.setDegreeAcronym(dgrRepo.findByCode(degreeCode).getAcronym());

        if (cenMemRepo.findByEmail(email) != null) {
            cenMemForm.setAgreement(true);
            return register(cenMemForm, model);
        }

        switch (tokenType) {
            case POSITION:
                cenMemForm.setPosition(positionToken.getPosition().getName());
                cenMemForm.setDiferentiator(positionToken.getDiferentiator());
                break;
            case CD:
                cenMemForm.setPosition("Consejo de Departamento " + cdToken.getDepartment().getAcronym());
                break;
            case COMMISSION:
                cenMemForm.setPosition(commissionToken.getCommission().getName());
                break;
        }

        model.addAttribute("censusMemberForm", cenMemForm);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
        CensusMemberDto censusMemberDto = null;
        TokenType tokenType = cenMemForm.getTokenType();

        CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());

        Boolean alreadyRegistered = true;
        Boolean errorFound = false;

        if (censusMember == null) {
            alreadyRegistered = false;
            if (!cenMemForm.getAgreement()) {
                errorFound = true;
                model.addAttribute("agreementError", "No se ha aceptado el acuerdo de usuario.");
            }

            if (!fieldValidator.validateEmail(cenMemForm.getEmail())) {
                errorFound = true;
                model.addAttribute("emailError", "No se ha introducido un email válido.");
            }

            if (!fieldValidator.validatePhone(cenMemForm.getPhone())) {
                errorFound = true;
                model.addAttribute("phoneError", "No se ha introducido un número de teléfono válido.");
            }

            if (!fieldValidator.validateID(cenMemForm.getPersonalID())) {
                errorFound = true;
                model.addAttribute("personalIDError", "El DNI/NIE introducido no es válido.");
            }

            if (!fieldValidator.emailExists(cenMemForm.getEmail())
                    && fieldValidator.IDExists(cenMemForm.getPersonalID())) {
                errorFound = true;
                model.addAttribute("personalIDError", "Un usuario con ese DNI/NIE ya está registrado.");
            }

            if (dgrRepo.findByCode(cenMemForm.getDegreeCode()) == null) {
                errorFound = true;
                model.addAttribute("degreeError", "La titulación especificada no se encuentra registrada.");
            }

            if (errorFound) {
                model.addAttribute("censusMemberForm", cenMemForm);
                return "register";
            }

            OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            censusMemberDto = new CensusMemberDto(
                    cenMemForm.getName(),
                    cenMemForm.getSurname(),
                    cenMemForm.getEmail(),
                    principal.getName(),
                    cenMemForm.getPersonalID(),
                    cenMemForm.getPhone(),
                    dgrRepo.findByCode(cenMemForm.getDegreeCode()));

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
                if (dlgRepo.findByPositionAndDiferentiatorAndAcademicYear(position, diferentiator, stngRepo.findBySettingKey("academicYear").getSettingValue()) != null) {
                    model.addAttribute("error", "Este cargo ya está registrado para este curso académico.");
                    return "error";
                }
                Delegate memberPosition = new Delegate(censusMember, position, degree, diferentiator, stngRepo.findBySettingKey("academicYear").getSettingValue());
                dlgRepo.saveAndFlush(memberPosition);
                break;

            case CD:
                CDToken cdToken = cdTknRepo.findByToken(cenMemForm.getToken());
                if (cdToken == null) {
                    model.addAttribute("error",
                            "No dispones de un token válido. Contacta con un administrador para continuar.");
                    return "error";
                }
                CDMember cdMember = new CDMember(censusMember, cdToken.getDepartment(), stngRepo.findBySettingKey("academicYear").getSettingValue(), false);
                cdMemRepo.saveAndFlush(cdMember);
                break;

            case COMMISSION:
                CommissionToken cmmToken = cmmTknRepo.findByToken(cenMemForm.getToken());
                if (cmmToken == null) {
                    model.addAttribute("error",
                            "No dispones de un token válido. Contacta con un administrador para continuar.");
                    return "error";
                }
                CommissionMember cmmMember = new CommissionMember(censusMember, cmmToken.getCommission(), stngRepo.findBySettingKey("academicYear").getSettingValue());
                cmmMemRepo.saveAndFlush(cmmMember);
                break;

            default:
                model.addAttribute("error",
                        "No dispones de un token válido. Contacta con un administrador para continuar.");
                return "error";
        }

        if (!alreadyRegistered) cenMemRepo.saveAndFlush(censusMember);

        return "redirect:profile";
    }
}