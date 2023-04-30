package es.upm.etsit.dat.identi.controllers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewMockupController {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @GetMapping("favicon.ico")
  String favicon() {
    return "forward:/favicon.svg";
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
  
  @GetMapping("/admin")
  public String admin(Model model) {
    return "admin/index";
  }

  @GetMapping("/admin/jd")
  public String jd(Model model) {
    return "admin/jd/index";
  }

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
