package es.upm.etsit.dat.identi.controllers.admin;

import java.net.http.HttpRequest;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.etsit.dat.identi.forms.GenerateTokenForm;
import es.upm.etsit.dat.identi.forms.TokenForm;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;
import es.upm.etsit.dat.identi.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        return "admin/tokens/index";
    }

    @GetMapping("/admin/tokens/delegates")
    public String delegateTokens(Model model) {
        model.addAttribute("tokens", tknRepo.findAll());
        return "admin/tokens/delegate_tokens";
    }

    @GetMapping("/admin/tokens/cds")
    public String cdTokens(Model model) {
        model.addAttribute("tokens", cdTknRepo.findAll());
        return "admin/tokens/cd_tokens";
    }

    @GetMapping("/admin/tokens/commissions")
    public String commissionTokens(Model model) {
        model.addAttribute("tokens", cmmTknRepo.findAll());
        return "admin/tokens/commission_tokens";
    }

    @GetMapping("/admin/tokens/generate")
    public String tokenForm(Model model, HttpServletRequest request) {
        List<Degree> degrees = dgrRepository.findAll();
        List<Position> positions = pstnRepository.findAll();
        model.addAttribute("degrees", degrees);
        model.addAttribute("positions", positions);
        model.addAttribute("_csrf", request.getAttribute("_csrf"));
        return "admin/tokens/generate_tokens";
    }
    
    @PostMapping("/admin/tokens/saveToken")
    @ResponseBody
    public String createToken(@RequestBody Object values, HttpServletResponse response) {
        
        Degree dat = dgrRepository.findByCode("09DA");
        Degree gitst = dgrRepository.findByCode("09TT");
        Degree gib = dgrRepository.findByCode("09IB");
        Degree gisd = dgrRepository.findByCode("09ID");

        Position deleEscuela = pstnRepository.findByName("Delegado/a de Escuela");
        Position subEscuela = pstnRepository.findByName("Subdelegado/a de Escuela");
        Position secretario = pstnRepository.findByName("Secretario/a");
        Position tesorero = pstnRepository.findByName("Tesorero/a");
        Position deleGrupo = pstnRepository.findByName("Delegado/a de grupo");
        Position deleCurso = pstnRepository.findByName("Delegado/a de curso");
        Position deleTitulacion = pstnRepository.findByName("Delegado/a de titulación");

        String[] elements = values.toString().replace("{", "").replace("}", "").split(",");
        Boolean datChecked = false;

        for (String element : elements) {
            String search = element.split("=")[0].trim();
            try {
                String value = element.split("=")[1];
            }
            catch (Exception e){
                String value = null;
            }
            
            System.out.println(search);
            switch (search){
                case "DAT":
                    datChecked = true;
                    break;

                case "DAT7":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, deleEscuela, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, deleEscuela, 0));
                    break;  

                case "DAT8":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, subEscuela, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, subEscuela, 0));
                    break;

                case "DAT9":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, secretario, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, secretario, 0));
                    break;

                case "DAT10":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, tesorero, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, tesorero, 0));
                    break;
            }
        }

        tknRepo.flush();
        response.setContentType("text/plain");
        return "pericos";
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
