package es.upm.etsit.dat.identi;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

  @GetMapping("/register")
  public String register(@AuthenticationPrincipal OAuth2User principal, /*@RequestParam(name = "email", required = false, defaultValue = "r.ggonzalez@alumnos.upm.es") String email,*/ Model model) {
    String email;
    String given_name;
    String family_name;
    try {
      email = principal.getAttribute("email");
      given_name = principal.getAttribute("given_name");
      family_name = principal.getAttribute("family_name");
    } catch (Exception e) {
      System.out.println("No hay ningún usuario logueado.");
      email = "";
      given_name = "";
      family_name = "";
    }

    model.addAttribute("email", email);
    model.addAttribute("given_name", given_name);
    model.addAttribute("family_name", family_name);

    return "register";
  }

  @GetMapping("/login")
  public String login(Model model) {
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

  @GetMapping("/admin/tokens")
  public String tokens(Model model) {
    return "tokens";
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
