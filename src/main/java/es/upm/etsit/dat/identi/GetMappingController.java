package es.upm.etsit.dat.identi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetMappingController {
    @GetMapping("/register")
    public String register(Model model) {
      return "register";
    }

    @GetMapping("/admin/census")
    public String census(Model model) {
      return "census";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
      return "profile";
    }
}
