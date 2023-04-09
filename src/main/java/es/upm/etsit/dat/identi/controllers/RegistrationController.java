package es.upm.etsit.dat.identi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.form.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;

@Controller
@SessionAttributes("censusMemberForm")
public class RegistrationController {
    @Autowired
    private CensusMemberService cenMemService;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @GetMapping("/register")
    public String registerForm(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email;
        String given_name;
        String family_name;
        try {
            email = principal.getAttribute("email");
            given_name = principal.getAttribute("given_name");
            family_name = principal.getAttribute("family_name");
        } catch (Exception e) {
            System.out.println("No hay ningún usuario logueado.");
            email = "p.perez@alumnos.upm.es";
            given_name = "Perico";
            family_name = "Pérez Pérez";
        }

        model.addAttribute("censusMemberForm",
                new CensusMemberForm(given_name, family_name, email, "GITST", "Delegado/a del grupo 34"));

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
        CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());
        if (censusMember == null) {
            cenMemService.create(new CensusMemberDto(cenMemForm.getName(), cenMemForm.getSurname(),
                    cenMemForm.getEmail(), cenMemForm.getPersonalID(), cenMemForm.getPhone(),
                    cenMemForm.getDegree()));
        } else {
            censusMember.setName(cenMemForm.getName());
            censusMember.setSurname(cenMemForm.getSurname());
            censusMember.setEmail(cenMemForm.getEmail());
            censusMember.setPersonalID(cenMemForm.getPersonalID());
            censusMember.setPhone(cenMemForm.getPhone());
            censusMember.setDegree(cenMemForm.getDegree());

            cenMemRepo.save(censusMember);
            cenMemRepo.flush();
        }

        return "redirect:profile";
    }
}
