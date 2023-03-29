package es.upm.etsit.dat.identi;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

  @GetMapping("/register")
  public String register(@AuthenticationPrincipal OAuth2User principal, /*@RequestParam(name = "email", required = false, defaultValue = "r.ggonzalez@alumnos.upm.es") String email,*/ Model model) {
    model.addAttribute("email", principal.getAttribute("email"));
    model.addAttribute("given_name", principal.getAttribute("given_name"));
    model.addAttribute("family_name", principal.getAttribute("family_name"));
    return "register";
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

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
