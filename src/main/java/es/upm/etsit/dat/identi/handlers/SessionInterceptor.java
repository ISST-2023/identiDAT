package es.upm.etsit.dat.identi.handlers;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import es.upm.etsit.dat.identi.User;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionInterceptor implements HandlerInterceptor {
    
    private CensusMemberRepository cenMemRepo;
    private String activeProfile;

    public SessionInterceptor (CensusMemberRepository cenMemRepo, String activeProfile) {
        this.cenMemRepo = cenMemRepo;
        this.activeProfile = activeProfile;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            if (this.activeProfile.equals("dev")) {
                session.setAttribute("user", new User("p.perez", true));
            } else {
                String username = request.getUserPrincipal().getName();
                CensusMember censusMember = this.cenMemRepo.findByUsername(username);
    
                session.setAttribute("user", new User(username, (censusMember != null) ? censusMember.getAdmin() : false));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        return;
    }
}