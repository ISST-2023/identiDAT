package es.upm.etsit.dat.identi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewMockupController {

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
    



    return "profiles";
  }

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
