package es.upm.etsit.dat.identi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.upm.etsit.dat.identi.forms.TokenForm;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;
import es.upm.etsit.dat.identi.service.TokenService;

@Controller
public class TokenController {
    @Autowired
    private TokenService tknService;

    @Autowired
    private TokenRepository tknRepo;

    @Autowired
    private CDTokenRepository cdTknRepo;

    @Autowired
    private CommissionTokenRepository cmmTknRepo;

    @Autowired
    private DegreeRepository dgrRepository;

    @Autowired
    private PositionRepository pstnRepository;

    @GetMapping("/admin/tokens")
    public String tokens(Model model) {
        return "tokens";
    }

    @GetMapping("/admin/tokens/delegates")
    public String delegateTokens(Model model) {
        model.addAttribute("tokens", tknRepo.findAll());
        return "delegate_tokens";
    }

    @GetMapping("/admin/tokens/cds")
    public String cdTokens(Model model) {
        model.addAttribute("tokens", cdTknRepo.findAll());
        return "cd_tokens";
    }

    @GetMapping("/admin/tokens/commissions")
    public String commissionTokens(Model model) {
        model.addAttribute("tokens", cmmTknRepo.findAll());
        return "commission_tokens";
    }

    @GetMapping("/admin/tokens/generate")
    public String tokenForm(Model model) {
        List<Degree> degrees = dgrRepository.findAll();
        List<Position> positions = pstnRepository.findAll();
        model.addAttribute("degrees", degrees);
        model.addAttribute("positions", positions);
        return "generate_tokens";
    }
    
    @PostMapping("/saveToken")
    public String createToken() {
        return "redirect:admin/tokens";
    }

    // Método generador de hash apartir de un string dado
    public String genToken(String key) {
        char[] caracteres = key.toCharArray();
        String[] caracteresSeparados = new String[caracteres.length];
        for (int i = 0; i < caracteres.length; i++) {
            caracteresSeparados[i] = String.valueOf(caracteres[i]);
        }

        String[] codes = new String[caracteresSeparados.length];

        for (int i = 0; i < caracteresSeparados.length; i++) {
            codes[i] = caracteresSeparados[i] + caracteresSeparados[i].codePointAt(0);
        }

        String hash = "";
        for (int i = 0; i < codes.length; i++) {
            hash = hash + codes[i];
        }
        return hash;
    }
}
