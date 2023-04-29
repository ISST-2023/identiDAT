package es.upm.etsit.dat.identi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;

@Controller
public class UserController {
    @Autowired
    private CDMemberRepository cdMemRepo;
  
    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Autowired
    private DelegateRepository dlgRepo;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
      String email = (String)principal.getAttribute("email");
      CensusMember userData = cenMemRepo.findByEmail(email);
      if (userData == null) {
        return "redirect:/register";
      }
      else {
        return "redirect:/profile";
      }
      
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
      String email = (String)principal.getAttribute("email");
      CensusMember userData = cenMemRepo.findByEmail(email);
      if (userData == null) {
        return "redirect:/register";
      }
      List<Delegate> userPositions = dlgRepo.findByCensusId(userData);
      List<CDMember> userCDs = cdMemRepo.findByCensusMember(userData);
      List<CommissionMember> userCommissions = cmmMemRepo.findByCensusMember(userData);
  
      model.addAttribute("censusMember", userData);
      model.addAttribute("positions", userPositions);
      model.addAttribute("cds", userCDs);
      model.addAttribute("commissions", userCommissions);
      return "profileview";
    }
}
