package es.upm.etsit.dat.identi.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.form.CensusMemberForm;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;

@Controller
public class ViewMockupController {
  @Autowired
  private CensusMemberService cenMemService;

  @Autowired
  private CensusMemberRepository cenMemRepo;

  @GetMapping("favicon.ico")
  String favicon() {
    return "forward:/favicon.svg";
  }

  @GetMapping("/")
  public String index(Model model) {
    return "redirect:/register";
  }

  @GetMapping("/login")
  public String login (Model model) {
    return "redirect:/oauth2/authorization/ssodat";
  }

  @GetMapping("/profile")
  public String profile(Model model) {
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
      form.setName(cenMem.getName());
      form.setSurname(cenMem.getSurname());
      form.setEmail(cenMem.getEmail());
      form.setPersonalID(cenMem.getPersonalID());
      form.setPhone(cenMem.getPhone());
      form.setDegree(cenMem.getDegree());

      params.put("form", form);
      return new ModelAndView("edit_user", params);
  }

  @PostMapping("/admin/profiles/edit")
    public String register(@ModelAttribute("form") CensusMemberForm cenMemForm, Model model) {
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

        return "redirect:/admin/profiles";
    }

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
