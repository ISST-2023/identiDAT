package es.upm.etsit.dat.identi.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;
import es.upm.etsit.dat.identi.User;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private CensusMemberRepository cenMemRepo;

    @GetMapping("/login")
    public String login (HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession();
        SavedRequest originalRequest = (SavedRequest)session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if (originalRequest != null) session.setAttribute("referrerUrl", originalRequest.getRedirectUrl());
        return "redirect:/oauth2/authorization/ssodat";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        OAuth2User principal = (OAuth2User)authentication.getPrincipal();
        CensusMember userData = cenMemRepo.findByUsername(principal.getName());

        if (userData == null) return "redirect:/register";
                
        HttpSession session = httpServletRequest.getSession();

        session.setAttribute("user", new User(principal.getName(), userData.getAdmin()));

        String referrerUrl = (String)session.getAttribute("referrerUrl");
        if (referrerUrl != null) {
            session.removeAttribute("referrerUrl");
            return "redirect:" + referrerUrl;
        }
        return "redirect:/";
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
