package es.upm.etsit.dat.identi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login (HttpServletRequest httpServletRequest, Model model) {
        return "redirect:/oauth2/authorization/ssodat";
    }

    @GetMapping("/logout")
    public void logout (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        httpServletResponse.setHeader("Location",
                "https://sso.dat.etsit.upm.es/realms/DAT-ETSIT/protocol/openid-connect/logout");
        httpServletResponse.setStatus(301);
    }
}
