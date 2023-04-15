package es.upm.etsit.dat.identi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;

@Controller
@SessionAttributes("censusMemberForm")
public class RegistrationController {
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

    @GetMapping("/register")
    public String registerForm(@AuthenticationPrincipal OAuth2User principal,
            @RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null) {
            model.addAttribute("error",
                    "No dispones de un token válido. Contacta con un administrador para continuar.");
            return "error";
        }

        Token positionToken = tknRepo.findByToken(token);

        if (positionToken == null) {
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

        CensusMemberForm cenMemForm = new CensusMemberForm(given_name, family_name, email,
                positionToken.getDegree().getAcronym(), positionToken.getToken());
        model.addAttribute("censusMemberForm", cenMemForm);

        if (cenMemRepo.findByEmail(email) != null) {
            cenMemForm.setAgreement(true);
            return register(cenMemForm, model);
        }

        model.addAttribute("position", positionToken.getPosition().getPosition());
        model.addAttribute("diferentiator", positionToken.getDiferentiator());

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
        if (!cenMemForm.getAgreement()) {
            model.addAttribute("error", "No se ha aceptado el acuerdo de usuario.");
            return "error";
        }

        CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());
        if (censusMember == null) {
            cenMemService.create(new CensusMemberDto(
                    cenMemForm.getName(), 
                    cenMemForm.getSurname(),
                    cenMemForm.getEmail(), 
                    cenMemForm.getPersonalID(),
                    cenMemForm.getPhone(),
                    dgrRepo.findByAcronym(cenMemForm.getDegree())));
        }

        censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());
        Token token = tknRepo.findByToken(cenMemForm.getToken());
        Position position = token.getPosition();
        Degree degree = token.getDegree();
        Integer diferentiator = token.getDiferentiator();

        Delegate memberPosition = new Delegate(censusMember, position, degree, diferentiator, 2023);
        if (dlgRepo.findByPositionIdAndDiferentiatorAndYear(position, diferentiator, 2023) != null) {
            model.addAttribute("error", "Este cargo ya está registrado para este curso académico.");
            return "error";
        }

        dlgRepo.save(memberPosition);

        return "redirect:profile";
    }
}
