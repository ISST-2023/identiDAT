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

import es.upm.etsit.dat.identi.persistence.model.CDToken;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionToken;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TokenController {
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

    @Autowired
    private DepartmentRepository dptRepository;

    @Autowired
    private CommissionRepository comsRepository;

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
        List<Department> departaments = dptRepository.findAll();
        List<Commission> commissions = comsRepository.findAll();
        model.addAttribute("degrees", degrees);
        model.addAttribute("positions", positions);
        model.addAttribute("departaments", departaments);
        model.addAttribute("commissions", commissions);
        
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

    @GetMapping("/admin/tokens/deletecd/{id}")
    public ModelAndView deletecdToken(@ModelAttribute("id") Long id) {
        try {
            cdTknRepo.deleteById(id);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/cds");
    }

    @GetMapping("/admin/tokens/deletecom/{id}")
    public ModelAndView deletecomToken(@ModelAttribute("id") Long id) {
        try {
            cmmTknRepo.deleteById(id);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/commissions");
    }

    @GetMapping("/admin/tokens/regenerate/{id}")
    public ModelAndView regenerateToken(@ModelAttribute("id") Long id) {
        try {
            Token t = tknRepo.findById(id).get();
            t.setToken(RandomStringUtils.randomAlphanumeric(64));
            tknRepo.save(t);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/delegates");
    }

    @GetMapping("/admin/tokens/regeneratecd/{id}")
    public ModelAndView regeneratecdToken(@ModelAttribute("id") Long id) {
        try {
            CDToken t = cdTknRepo.findById(id).get();
            t.setToken(RandomStringUtils.randomAlphanumeric(64));
            cdTknRepo.save(t);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/cds");
    }

    @GetMapping("/admin/tokens/regeneratecom/{id}")
    public ModelAndView regeneratecomToken(@ModelAttribute("id") Long id) {
        try {
            CommissionToken t = cmmTknRepo.findById(id).get();
            t.setToken(RandomStringUtils.randomAlphanumeric(64));
            cmmTknRepo.save(t);
        } catch (Exception e) {
        }
        return new ModelAndView("redirect:/admin/tokens/commissions");
    }
    
    @PostMapping("/admin/tokens/saveToken")
    @ResponseBody
    public String createToken(@RequestBody Object values, HttpServletResponse response) {
        System.out.println(values);
        
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
        
        //String regexp = "/([1-4][1-6]-?)+/";
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
        Boolean[] cdChecked = new Boolean[9];
        Boolean[] comisChecked = new Boolean[10]; 
        Arrays.fill(gitstChecked, false);
        Arrays.fill(gibChecked, false);
        Arrays.fill(gisdChecked, false);
        Arrays.fill(muitChecked, false);
        Arrays.fill(muirstChecked, false);
        Arrays.fill(muiseChecked, false);
        Arrays.fill(mutscChecked, false);
        Arrays.fill(muesfvChecked, false);
        Arrays.fill(muibChecked, false);
        Arrays.fill(cdChecked, false);
        Arrays.fill(comisChecked, false);

        for (String element : elements) {
            String search = element.split("=")[0].trim();
            String value;
            try {
                value = element.split("=")[1];
            }
            catch (Exception e) {
                value = null;
            }
            
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
                    if (gitstChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gitst, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, deleTitulacion, 0));
                    break;
                case "GITST6":
                    if (gitstChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gitst, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, subDeleTitulacion, 0));
                    break;
                case "GITSTgroups1":
                    groupTokenGen(gitstChecked[0], gitstChecked[1], value, gitst, deleGrupo);
                    break;
                case "GITSTgroups2":
                    groupTokenGen(gitstChecked[0], gitstChecked[2], value, gitst, subDeleGrupo);
                    break;
                case "GITSTgroups3":
                    groupTokenGen(gitstChecked[0], gitstChecked[3], value, gitst, deleCurso);
                    break;
                case "GITSTgroups4":
                    groupTokenGen(gitstChecked[0], gitstChecked[4], value, gitst, subDeleCurso);
                    break;



                case "GIB":
                    gibChecked[0] = true;
                    break;
                case "GIB1":
                    gibChecked[1] = true;
                    break;
                case "GIB2":
                    gibChecked[2] = true;
                    break;
                case "GIB3":
                    gibChecked[3] = true;
                    break;
                case "GIB4":
                    gibChecked[4] = true;
                    break;
                case "GIB5":
                    if (gibChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gib, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gib, deleTitulacion, 0));
                    break;
                case "GIB6":
                    if (gibChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gib, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gib, subDeleTitulacion, 0));
                    break;
                case "GIBgroups1":
                    groupTokenGen(gibChecked[0], gibChecked[1], value, gib, deleGrupo);
                    break;
                case "GIBgroups2":
                    groupTokenGen(gibChecked[0], gibChecked[2], value, gib, subDeleGrupo);
                    break;
                case "GIBgroups3":
                    groupTokenGen(gibChecked[0], gibChecked[3], value, gib, deleCurso);
                    break;
                case "GIBgroups4":
                    groupTokenGen(gibChecked[0], gibChecked[4], value, gib, subDeleCurso);
                    break;


                case "GISD":
                    gisdChecked[0] = true;
                    break;
                case "GISD1":
                    gisdChecked[1] = true;
                    break;
                case "GISD2":
                    gisdChecked[2] = true;
                    break;
                case "GISD3":
                    gisdChecked[3] = true;
                    break;
                case "GISD4":
                    gisdChecked[4] = true;
                    break;
                case "GISD5":
                    if (gisdChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gisd, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gisd, deleTitulacion, 0));
                    break;
                case "GISD6":
                    if (gisdChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(gisd, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), gisd, subDeleTitulacion, 0));
                    break;
                case "GISDgroups1":
                    groupTokenGen(gisdChecked[0], gisdChecked[1], value, gisd, deleGrupo);
                    break;
                case "GISDgroups2":
                    groupTokenGen(gisdChecked[0], gisdChecked[2], value, gisd, subDeleGrupo);
                    break;
                case "GISDgroups3":
                    groupTokenGen(gisdChecked[0], gisdChecked[3], value, gisd, deleCurso);
                    break;
                case "GISDgroups4":
                    groupTokenGen(gisdChecked[0], gisdChecked[4], value, gisd, subDeleCurso);
                    break;

                
                case "MUIT":
                    muitChecked[0] = true;
                    break;
                case "MUIT1":
                    muitChecked[1] = true;
                    break;
                case "MUIT2":
                    muitChecked[2] = true;
                    break;
                case "MUIT3":
                    muitChecked[3] = true;
                    break;
                case "MUIT4":
                    muitChecked[4] = true;
                    break;
                case "MUIT5":
                    if (muitChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muit, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muit, deleTitulacion, 0));
                    break;
                case "MUIT6":
                    if (muitChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muit, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muit, subDeleTitulacion, 0));
                    break;
                case "MUITgroups1":
                    groupTokenGen(muitChecked[0], muitChecked[1], value, muit, deleGrupo);
                    break;
                case "MUITgroups2":
                    groupTokenGen(muitChecked[0], muitChecked[2], value, muit, subDeleGrupo);
                    break;
                case "MUITgroups3":
                    groupTokenGen(muitChecked[0], muitChecked[3], value, muit, deleCurso);
                    break;
                case "MUITgroups4":
                    groupTokenGen(muitChecked[0], muitChecked[4], value, muit, subDeleCurso);
                    break;


                case "MUIRST":
                    muirstChecked[0] = true;
                    break;
                case "MUIRST1":
                    muirstChecked[1] = true;
                    break;
                case "MUIRST2":
                    muirstChecked[2] = true;
                    break;
                case "MUIRST3":
                    muirstChecked[3] = true;
                    break;
                case "MUIRST4":
                    muirstChecked[4] = true;
                    break;
                case "MUIRST5":
                    if (muirstChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muirst, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muirst, deleTitulacion, 0));
                    break;
                case "MUIRST6":
                    if (muirstChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muirst, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muirst, subDeleTitulacion, 0));
                    break;
                case "MUIRSTgroups1":
                    groupTokenGen(muirstChecked[0], muirstChecked[1], value, muirst, deleGrupo);
                    break;
                case "MUIRSTgroups2":
                    groupTokenGen(muirstChecked[0], muirstChecked[2], value, muirst, subDeleGrupo);
                    break;
                case "MUIRSTgroups3":
                    groupTokenGen(muirstChecked[0], muirstChecked[3], value, muirst, deleCurso);
                    break;
                case "MUIRSTgroups4":
                    groupTokenGen(muirstChecked[0], muirstChecked[4], value, muirst, subDeleCurso);
                    break;

                case "MUISE":
                    muiseChecked[0] = true;
                    break;
                case "MUISE1":
                    muiseChecked[1] = true;
                    break;
                case "MUISE2":
                    muiseChecked[2] = true;
                    break;
                case "MUISE3":
                    muiseChecked[3] = true;
                    break;
                case "MUISE4":
                    muiseChecked[4] = true;
                    break;
                case "MUISE5":
                    if (muiseChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muise, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muise, deleTitulacion, 0));
                    break;
                case "MUISE6":
                    if (muiseChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muise, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muise, subDeleTitulacion, 0));
                    break;
                case "MUISEgroups1":
                    groupTokenGen(muiseChecked[0], muiseChecked[1], value, muise, deleGrupo);
                    break;
                case "MUISEgroups2":
                    groupTokenGen(muiseChecked[0], muiseChecked[2], value, muise, subDeleGrupo);
                    break;
                case "MUISEgroups3":
                    groupTokenGen(muiseChecked[0], muiseChecked[3], value, muise, deleCurso);
                    break;
                case "MUISEgroups4":
                    groupTokenGen(muiseChecked[0], muiseChecked[4], value, muise, subDeleCurso);
                    break;
                

                case "MUTSC":
                    mutscChecked[0] = true;
                    break;
                case "MUTSC1":
                    mutscChecked[1] = true;
                    break;
                case "MUTSC2":
                    mutscChecked[2] = true;
                    break;
                case "MUTSC3":
                    mutscChecked[3] = true;
                    break;
                case "MUTSC4":
                    mutscChecked[4] = true;
                    break;
                case "MUTSC5":
                    if (mutscChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(mutsc, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), mutsc, deleTitulacion, 0));
                    break;
                case "MUTSC6":
                    if (mutscChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(mutsc, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), mutsc, subDeleTitulacion, 0));
                    break;
                case "MUTSCgroups1":
                    groupTokenGen(mutscChecked[0], mutscChecked[1], value, mutsc, deleGrupo);
                    break;
                case "MUTSCgroups2":
                    groupTokenGen(mutscChecked[0], mutscChecked[2], value, mutsc, subDeleGrupo);
                    break;
                case "MUTSCgroups3":
                    groupTokenGen(mutscChecked[0], mutscChecked[3], value, mutsc, deleCurso);
                    break;
                case "MUTSCgroups4":
                    groupTokenGen(mutscChecked[0], mutscChecked[4], value, mutsc, subDeleCurso);
                    break;
                

                case "MUESFV":
                    muesfvChecked[0] = true;
                    break;
                case "MUESFV1":
                    muesfvChecked[1] = true;
                    break;
                case "MUESFV2":
                    muesfvChecked[2] = true;
                    break;
                case "MUESFV3":
                    muesfvChecked[3] = true;
                    break;
                case "MUESFV4":
                    muesfvChecked[4] = true;
                    break;
                case "MUESFV5":
                    if (muesfvChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muesfv, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muesfv, deleTitulacion, 0));
                    break;
                case "MUESFV6":
                    if (muesfvChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muesfv, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muesfv, subDeleTitulacion, 0));
                    break;
                case "MUESFVgroups1":
                    groupTokenGen(muesfvChecked[0], muesfvChecked[1], value, muesfv, deleGrupo);
                    break;
                case "MUESFVgroups2":
                    groupTokenGen(muesfvChecked[0], muesfvChecked[2], value, muesfv, subDeleGrupo);
                    break;
                case "MUESFVgroups3":
                    groupTokenGen(muesfvChecked[0], muesfvChecked[3], value, muesfv, deleCurso);
                    break;
                case "MUESFVgroups4":
                    groupTokenGen(muesfvChecked[0], muesfvChecked[4], value, muesfv, subDeleCurso);
                    break;


                case "MUIB":
                    muibChecked[0] = true;
                    break;
                case "MUIB1":
                    muibChecked[1] = true;
                    break;
                case "MUIB2":
                    muibChecked[2] = true;
                    break;
                case "MUIB3":
                    muibChecked[3] = true;
                    break;
                case "MUIB4":
                    muibChecked[4] = true;
                    break;
                case "MUIB5":
                    if (muibChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muib, deleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muib, deleTitulacion, 0));
                    break;
                case "MUIB6":
                    if (muibChecked[0] && tknRepo.findByDegreeAndPositionAndDiferentiator(muib, subDeleTitulacion, 0) == null)
                        tknRepo.save(new Token(RandomStringUtils.randomAlphanumeric(64), muib, subDeleTitulacion, 0));
                    break;
                case "MUIBgroups1":
                    groupTokenGen(muibChecked[0], muibChecked[1], value, muib, deleGrupo);
                    break;
                case "MUIBgroups2":
                    groupTokenGen(muibChecked[0], muibChecked[2], value, muib, subDeleGrupo);
                    break;
                case "MUIBgroups3":
                    groupTokenGen(muibChecked[0], muibChecked[3], value, muib, deleCurso);
                    break;
                case "MUIBgroups4":
                    groupTokenGen(muibChecked[0], muibChecked[4], value, muib, subDeleCurso);
                    break;


                case "CD":
                    cdChecked[0] = true;
                    break;
                case "DIT":
                    cdChecked[1] = true;
                    break;
                case "SSR":
                    cdChecked[2] = true;
                    break;
                case "DIE":
                    cdChecked[3] = true;
                    break;
                case "ELF":
                    cdChecked[4] = true;
                    break;
                case "MAT":
                    cdChecked[5] = true;
                    break;
                case "TFB":
                    cdChecked[6] = true;
                    break;
                case "IOR":
                    cdChecked[7] = true;
                    break;
                case "LIA":
                    cdChecked[8] = true;
                    break;
                case "DITnumber":
                    Department dit = dptRepository.findByAcronym("DIT");
                    cdComGenToken(cdChecked[0], cdChecked[1], value, dit, null, 0);
                    break;
                case "SSRnumber":
                    Department ssr = dptRepository.findByAcronym("SSR");
                    cdComGenToken(cdChecked[0], cdChecked[2], value, ssr, null, 0);
                    break;
                case "DIEnumber":
                    Department die = dptRepository.findByAcronym("DIE");
                    cdComGenToken(cdChecked[0], cdChecked[3], value, die, null, 0);
                    break;
                case "ELFnumber":
                    Department elf = dptRepository.findByAcronym("ELF");
                    cdComGenToken(cdChecked[0], cdChecked[4], value, elf, null, 0);
                    break;
                case "MATnumber":
                    Department mat = dptRepository.findByAcronym("MAT");
                    cdComGenToken(cdChecked[0], cdChecked[5], value, mat, null, 0);
                    break;
                case "TFBnumber":
                    Department tfb = dptRepository.findByAcronym("TFB");
                    cdComGenToken(cdChecked[0], cdChecked[6], value, tfb, null, 0);
                    break;
                case "IORnumber":
                    Department ior = dptRepository.findByAcronym("IOR");
                    cdComGenToken(cdChecked[0], cdChecked[7], value, ior, null, 0);
                    break;
                case "LIAnumber":
                    Department lia = dptRepository.findByAcronym("LIA");
                    cdComGenToken(cdChecked[0], cdChecked[8], value, lia, null, 0);
                    break;

                case "Comisiones":
                    comisChecked[0] = true;
                    break;
                case "Claustro Universitario":
                    comisChecked[1] = true;
                    break;
                case "Comisión de Calidad":
                    comisChecked[2] = true;
                    break;
                case "Comisión de Gobierno":
                    comisChecked[3] = true;
                    break;
                case "Comisión de Ordenación Académica":
                    comisChecked[4] = true;
                    break;
                case "Comisión Electoral Central":
                    comisChecked[5] = true;
                    break;
                case "Comisión Electoral de Centro":
                    comisChecked[6] = true;
                    break;
                case "Junta de Delegados UPM":
                    comisChecked[7] = true;
                    break;
                case "Junta de Escuela":
                    comisChecked[8] = true;
                    break;
                case "Junta de Representantes UPM":
                    comisChecked[9] = true;
                    break;
                case "Claustro Universitarionumber":
                    Commission claustro = comsRepository.findByName("Claustro Universitario");
                    cdComGenToken(comisChecked[0], comisChecked[1], value, null, claustro, 1);
                    break;
                case "Comisión de Calidadnumber":
                    Commission calidad = comsRepository.findByName("Comisión de Calidad");
                    cdComGenToken(comisChecked[0], comisChecked[2], value, null, calidad, 1);
                    break;
                case "Comisión de Gobiernonumber":
                    Commission gobierno = comsRepository.findByName("Comisión de Gobierno");
                    cdComGenToken(comisChecked[0], comisChecked[3], value, null, gobierno, 1);
                    break;
                case "Comisión de Ordenación Académicanumber":
                    Commission coa = comsRepository.findByName("Comisión de Ordenación Académica");
                    cdComGenToken(comisChecked[0], comisChecked[4], value, null, coa, 1);
                    break;
                case "Comisión Electoral Centralnumber":
                    Commission cecentral = comsRepository.findByName("Comisión Electoral Central");
                    cdComGenToken(comisChecked[0], comisChecked[5], value, null, cecentral, 1);
                    break;
                case "Comisión Electoral de Centronumber":
                    Commission cecentro = comsRepository.findByName("Comisión Electoral de Centro");
                    cdComGenToken(comisChecked[0], comisChecked[6], value, null, cecentro, 1);
                    break;
                case "Junta de Delegados UPMnumber":
                    Commission jd = comsRepository.findByName("Junta de Delegados UPM");
                    cdComGenToken(comisChecked[0], comisChecked[7], value, null, jd, 1);
                    break;
                case "Junta de Escuelanumber":
                    Commission je = comsRepository.findByName("Junta de Escuela");
                    cdComGenToken(comisChecked[0], comisChecked[8], value, null, je, 1);
                    break;
                case "Junta de Representantes UPMnumber":
                    Commission jr = comsRepository.findByName("Junta de Representantes UPM");
                    cdComGenToken(comisChecked[0], comisChecked[9], value, null, jr, 1);
                    break;
            }
        }

        tknRepo.flush();
        cdTknRepo.flush();
        cmmTknRepo.flush();
        response.setContentType("text/plain");
        return "ok";
    }

    private void cdComGenToken(Boolean topCheck, Boolean prevCheck, String value, Department dpt, Commission cms, Integer diff) {
        if(value != null && topCheck && prevCheck) {
            Integer number;
            try {
                number = Integer.valueOf(value);
            }
            catch (Exception e){
                return;
            }
            while(number > 0) {
                if (diff == 0)
                    cdTknRepo.save(new CDToken(RandomStringUtils.randomAlphanumeric(64), dpt));
                else
                    cmmTknRepo.save(new CommissionToken(RandomStringUtils.randomAlphanumeric(64), cms));
                number--;
            }
        }
    }

    private void groupTokenGen(Boolean topCheck, Boolean prevCheck, String value, Degree dgr, Position pst) {
        if(topCheck && prevCheck) {
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
