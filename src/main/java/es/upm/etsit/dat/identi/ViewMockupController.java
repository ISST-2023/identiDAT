package es.upm.etsit.dat.identi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.dto.TokenDto;
import es.upm.etsit.dat.identi.form.CensusMemberForm;
import es.upm.etsit.dat.identi.service.CensusMemberService;
import es.upm.etsit.dat.identi.service.TokenService;
import es.upm.etsit.dat.identi.form.TokenForm;

@Controller
public class ViewMockupController {

  @Autowired
  CensusMemberService cenMemService;

  @Autowired
  TokenService tokenService;



  @GetMapping("favicon.ico")
  String favicon() {
    return "forward:/favicon.svg";
  }

  @GetMapping("/")
  public String index(Model model) {
    return "redirect:/register";
  }

  @GetMapping("/register")
  public String register(@AuthenticationPrincipal OAuth2User principal, /*@RequestParam(name = "email", required = false, defaultValue = "r.ggonzalez@alumnos.upm.es") String email,*/ Model model) {
    String email;
    String given_name;
    String family_name;
    try {
      email = principal.getAttribute("email");
      given_name = principal.getAttribute("given_name");
      family_name = principal.getAttribute("family_name");
    } catch (Exception e) {
      System.out.println("No hay ningún usuario logueado.");
      email = "";
      given_name = "";
      family_name = "";
    }

    model.addAttribute("email", email);
    model.addAttribute("given_name", given_name);
    model.addAttribute("family_name", family_name);

    model.addAttribute("censusMemberForm", new CensusMemberForm());

    return "register";
  }

  @PostMapping("/register")
  public String createCensusMember(@ModelAttribute CensusMemberForm censusMember){
    cenMemService.create(new CensusMemberDto(censusMember.getName(), censusMember.getSurname(), censusMember.getEmail(), censusMember.getPersonalID(), censusMember.getPhone(), censusMember.getDegree()));
    return "redirect:profile";
  }

  @PostMapping("/saveToken")
  public String createToken(@ModelAttribute TokenForm token){
    //Variables globales para los tokens
    String unToken = "";
    String[] years = {"1","2","3","4"};
    String[] groups = {"1","2","3","4","5"};
    String[] positions = {"delegado","subdelegado"};

    String degree = token.getDegree();
    String year = token.getYear();
    String group = token.getGrupo();
    String position = token.getPosition();
    String concat = "";
    //tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(year), Integer.parseInt(year + group), position));
    
    if (degree.equals("GITST")) {
      if (year.equals("All")) {

        if (group.equals("All")) {
          
          if (position.equals("All")) {

            for(int i = 0; i < years.length; i++){
              for(int j = 0; j < groups.length; j++){
                for(int k = 0; k < positions.length; k ++){
                  concat = degree + years[i] + years[i] + groups[j] + positions[k];
                  unToken = genToken(concat);
                  tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]), Integer.parseInt(years[i] + groups[j]), positions[k]));
                }
              }
            }

          } else {

            for(int i = 0; i < years.length; i++){
              for(int j = 0; j < groups.length; j++){
                concat = degree + years[i] + years[i] + groups[j] + position;
                unToken = genToken(concat);
                tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]), Integer.parseInt(years[i] + groups[j]), position));             
              }
            }

          }

        } else {

          if (position.equals("All")) {

            for(int i = 0; i < years.length; i++){             
              for(int k = 0; k < positions.length; k ++){
                concat = degree + years[i] + years[i] + group + positions[k];
                unToken = genToken(concat);
                tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]), Integer.parseInt(years[i] + group), positions[k]));
              }
            }

          } else {

            for(int i = 0; i < years.length; i++){
              concat = degree + years[i] + years[i] + group + position;
              unToken = genToken(concat);
              tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]), Integer.parseInt(years[i] + group), position));             
            }

          }

        }

      } else {
          
        if (group.equals("All")) {

          if (position.equals("All")) {

            for(int j = 0; j < groups.length; j++){
              for(int k = 0; k < positions.length; k ++){
                concat = degree + year + year + groups[j] + positions[k];
                unToken = genToken(concat);
                tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(year), Integer.parseInt(year + groups[j]), positions[k]));
              }
            }
            
          } else {
  
            for(int j = 0; j < groups.length; j++){
              concat = degree + year + year + groups[j] + position;
              unToken = genToken(concat);
              tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(year), Integer.parseInt(year + groups[j]), position));             
            }
          
          }

        } else {

          if (position.equals("All")) {

            for(int k = 0; k < positions.length; k ++){
              concat = degree + year + year + group + positions[k];
              unToken = genToken(concat);
              tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(year), Integer.parseInt(year + group), positions[k]));
            }
            
          } else {
      
            concat = degree + year + year + group + position;
            unToken = genToken(concat);
            tokenService.create(new TokenDto(unToken, degree, Integer.parseInt(year), Integer.parseInt(year + group), position));             
            
          }
            
        }

      }

    } else if (degree.equals("GIB")) {
      //rellenar
    } else if (degree.equals("MUIT")) {
      //rellenar
    } else if (degree.equals("datos")) {
      //rellenar
    }
    
    return "redirect:admin/tokens";
  }

  //Función generadora de hash apartir de un string dado
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


  @GetMapping("/login")
  public String login(Model model) {
    return "redirect:/oauth2/authorization/ssodat";
  }

  @GetMapping("/profile")
  public String profile(Model model) {
    return "profileview";
  }
  
  @GetMapping("/admin")
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

  @GetMapping("/admin/tokens")
  public String tokens(Model model) {
    return "tokens";
  }

  @GetMapping("/admin/profiles")
  public String list(Model model) {
    return "profiles";
  }

  @GetMapping("/admin/assistance")
  public String assistance(Model model) {
    return "assistance_jd";
  }
}
