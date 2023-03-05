package es.upm.etsit.dat.identi;

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
    return "index";
  }

  @GetMapping("/register")
  public String register(
      @RequestParam(name = "email", required = false, defaultValue = "r.ggonzalez@alumnos.upm.es") String email,
      Model model) {
    model.addAttribute("email", email);
    return "register";
  }

  @GetMapping("/admin/")
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
