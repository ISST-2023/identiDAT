package es.upm.etsit.dat.identi.controllers.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/admin/tokens/delete/{id}")
    public ModelAndView deleteToken(@ModelAttribute("id") Long id) {
        try {
            tknRepo.deleteById(id);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/delegates");
    }
    
    @PostMapping("/admin/tokens/saveToken")
    @ResponseBody
    public String createToken(@RequestBody Object values, HttpServletResponse response) {
        
        Degree dat = dgrRepository.findByCode("09DA");
        Degree gitst = dgrRepository.findByCode("09TT");
        Degree gib = dgrRepository.findByCode("09IB");
        Degree gisd = dgrRepository.findByCode("09ID");
        Degree muit = dgrRepository.findByCode("09AQ");
        Degree muirst = dgrRepository.findByCode("09AS");
        Degree muise = dgrRepository.findByCode("09AZ");
        Degree mutsc = dgrRepository.findByCode("09AT");
        Degree muesfv = dgrRepository.findByCode("09AX");
        Degree muib = dgrRepository.findByCode("09AU");

        Position deleGrupo = pstnRepository.findByName("Delegado/a de grupo");
        Position subDeleGrupo = pstnRepository.findByName("Subdelegado/a de grupo");
        Position deleCurso = pstnRepository.findByName("Delegado/a de curso");
        Position subDeleCurso = pstnRepository.findByName("Subdelegado/a de curso");
        Position deleTitulacion = pstnRepository.findByName("Delegado/a de titulación");
        Position subDeleTitulacion = pstnRepository.findByName("Subdelegado/a de titulación");
        Position deleEscuela = pstnRepository.findByName("Delegado/a de Escuela");
        Position subDeleEscuela = pstnRepository.findByName("Subdelegado/a de Escuela");
        Position secretario = pstnRepository.findByName("Secretario/a");
        Position tesorero = pstnRepository.findByName("Tesorero/a");
        

        String[] elements = values.toString().replace("{", "").replace("}", "").split(",");
        
        String regexp = "/([1-4][1-6]-?)+/";
        Boolean datChecked = false;
        Boolean[] gitstChecked = new Boolean[5];
        Boolean[] gibChecked = new Boolean[5];
        Boolean[] gisdChecked = new Boolean[5];
        Boolean[] muitChecked = new Boolean[5];
        Boolean[] muirstChecked = new Boolean[5];
        Boolean[] muiseChecked = new Boolean[5];
        Boolean[] mutscChecked = new Boolean[5];
        Boolean[] muesfvChecked = new Boolean[5];
        Boolean[] muibChecked = new Boolean[5];
        Arrays.fill(gitstChecked, false);
        Arrays.fill(gibChecked, false);
        Arrays.fill(gisdChecked, false);
        Arrays.fill(muitChecked, false);
        Arrays.fill(muirstChecked, false);
        Arrays.fill(muiseChecked, false);
        Arrays.fill(mutscChecked, false);
        Arrays.fill(muesfvChecked, false);
        Arrays.fill(muibChecked, false);

        for (String element : elements) {
            String search = element.split("=")[0].trim();
            String value;
            try {
                value = element.split("=")[1];
            }
            catch (Exception e) {
                value = null;
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
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, subDeleEscuela, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, subDeleEscuela, 0));
                    break;
                case "DAT9":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, secretario, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, secretario, 0));
                    break;
                case "DAT10":
                    if (datChecked && tknRepo.findByDegreeAndPositionAndDiferentiator(dat, tesorero, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dat, tesorero, 0));
                    break;

                case "GITST":
                    gitstChecked[0] = true;
                    break;
                case "GITST1":
                    gitstChecked[1] = true;
                    break;
                case "GITST2":
                    gitstChecked[2] = true;
                    break;
                case "GITST3":
                    gitstChecked[3] = true;
                    break;
                case "GITST4":
                    gitstChecked[4] = true;
                    break;
                case "GITST5":
                    if (tknRepo.findByDegreeAndPositionAndDiferentiator(gitst, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, deleTitulacion, 0));
                    break;
                case "GITST6":
                    if (tknRepo.findByDegreeAndPositionAndDiferentiator(gitst, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, subDeleTitulacion, 0));
                    break;
                case "GITSTgroups1":
                    groupTokenGen(gitstChecked[1], value, gitst, deleGrupo);
                    break;
                case "GITSTgroups2":
                    groupTokenGen(gitstChecked[2], value, gitst, subDeleGrupo);
                    break;
                case "GITSTgroups3":
                    groupTokenGen(gitstChecked[3], value, gitst, deleCurso);
                    break;
                case "GITSTgroups4":
                    groupTokenGen(gitstChecked[4], value, gitst, subDeleCurso);
                    break;
            }
        }

        tknRepo.flush();
        response.setContentType("text/plain");
        return "pericos";
    }

    private void groupTokenGen(Boolean prevCheck, String value, Degree dgr, Position pst) {
        if(prevCheck) {
            String[] groups = value.split("-");
            for (String group : groups){
                Integer grupo;
                try {
                    grupo = Integer.valueOf(group);
                }
                catch (Exception e){
                    continue;
                }
                if (tknRepo.findByDegreeAndPositionAndDiferentiator(dgr, pst, grupo) == null)
                    tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), dgr, pst, grupo));
            }
            
        }
    }
}
