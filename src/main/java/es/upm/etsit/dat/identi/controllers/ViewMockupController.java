package es.upm.etsit.dat.identi.controllers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ViewMockupController {
  @Autowired
  private CensusMemberService cenMemService;

  @Autowired
  private CensusMemberRepository cenMemRepo;

  @Autowired
  private DelegateRepository dlgRepo;

  @Autowired
  private DegreeRepository dgrRepo;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @GetMapping("favicon.ico")
  String favicon() {
    return "forward:/favicon.svg";
  }

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

  @GetMapping("/test")
  public String test(HttpSession session, Model model) {
    final Enumeration<String> attributeNames = session.getAttributeNames();
    final Map<String, Object> attributes = new HashMap<String, Object> ();
    attributes.put("Active Profile", activeProfile);
    do {
      String attrib = attributeNames.nextElement();
      attributes.put(attrib, session.getAttribute(attrib));
    } while (attributeNames.hasMoreElements());
    
    model.addAttribute("attributes", attributes);
    return "test";
  }

  @GetMapping("/profile")
  public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
    String email = (String)principal.getAttribute("email");
    CensusMember userData = cenMemRepo.findByEmail(email);
    if (userData == null) {
      return "redirect:/register";
    }
    List<Delegate> userPositions = dlgRepo.findByCensusId(userData);
    model.addAttribute("censusMember", userData);
    model.addAttribute("positions", userPositions);
    return "profileview";
  }
  
  @GetMapping("/admin")
  public String admin(Model model) {
    return "admin";
  }

  @GetMapping("/admin/census")
  public String census(Model model) {
    return "census";
  }

  @GetMapping("/admin/jd")
  public String jd(Model model) {
    return "jd";
  }

  @GetMapping("/admin/profiles")
  public String list(Model model) {
    List<CensusMemberDto> censusMembers =  cenMemService.getAll();
    model.addAttribute("censusMembers", censusMembers);
    return "profiles";
  }

  @GetMapping("/admin/profiles/edit/{id}")
  public ModelAndView edit(@ModelAttribute("id") Long id) {
      var params = new HashMap<String, Object>();
      CensusMemberForm form = new CensusMemberForm();
      CensusMemberDto cenMem = cenMemService.get(id);
      form.setId(cenMem.getId());
      form.setName(cenMem.getName());
      form.setSurname(cenMem.getSurname());
      form.setEmail(cenMem.getEmail());
      form.setPersonalID(cenMem.getPersonalID());
      form.setPhone(cenMem.getPhone());
      form.setDegree(cenMem.getDegree().getAcronym());

      params.put("form", form);
      return new ModelAndView("edit_user", params);
  }

  @PostMapping("/admin/profiles/edit")
    public String register(@ModelAttribute("form") CensusMemberForm cenMemForm, Model model) {
        Optional<CensusMember> censusMember = cenMemRepo.findById(cenMemForm.getId());

        if (censusMember.isPresent()) {
          CensusMember member = censusMember.get();
          member.setName(cenMemForm.getName());
          member.setSurname(cenMemForm.getSurname());
          member.setEmail(cenMemForm.getEmail());
          member.setPersonalID(cenMemForm.getPersonalID());
          member.setPhone(cenMemForm.getPhone());
          member.setDegree(dgrRepo.findByAcronym(cenMemForm.getDegree()));

          cenMemRepo.save(member);
          cenMemRepo.flush();
        }     

        return "redirect:/admin/profiles";
    }

  @GetMapping("/admin/profiles/delete/{id}")
  public ModelAndView delete(@ModelAttribute("id") Long id) {
    cenMemService.delete(id);
    return new ModelAndView("redirect:/admin/profiles");
  }

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
