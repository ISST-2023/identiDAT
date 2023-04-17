package es.upm.etsit.dat.identi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.upm.etsit.dat.identi.dto.TokenDto;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.service.TokenService;

@Controller
public class TokenController {
    @Autowired
    private TokenService tknService;

    @Autowired
    private DegreeRepository dgrRepository;

    @Autowired
    private PositionRepository pstnRepository;

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
        /* // Variables globales para los tokens
        String unToken = "";
        String[] years = { "1", "2", "3", "4" };
        String[] groups = { "1", "2", "3", "4", "5" };
        String[] positions = { "delegado", "subdelegado" };

        // variable ssacadas del formulario
        String degree = token.getDegree();
        String year = token.getYear();
        String group = token.getGrupo();
        String position = token.getPosition();

        // varibale auxiliar
        String concat = "";

        if (year.equals("All")) {

            if (group.equals("All")) {

                if (position.equals("All")) {

                    for (int i = 0; i < years.length; i++) {
                        for (int j = 0; j < groups.length; j++) {
                            for (int k = 0; k < positions.length; k++) {
                                concat = degree + years[i] + years[i] + groups[j] + positions[k];
                                unToken = genToken(concat);
                                tknService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]),
                                        Integer.parseInt(years[i] + groups[j]), positions[k]));
                            }
                        }
                    }

                } else {

                    for (int i = 0; i < years.length; i++) {
                        for (int j = 0; j < groups.length; j++) {
                            concat = degree + years[i] + years[i] + groups[j] + position;
                            unToken = genToken(concat);
                            tknService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]),
                                    Integer.parseInt(years[i] + groups[j]), position));
                        }
                    }

                }

            } else {

                if (position.equals("All")) {

                    for (int i = 0; i < years.length; i++) {
                        for (int k = 0; k < positions.length; k++) {
                            concat = degree + years[i] + years[i] + group + positions[k];
                            unToken = genToken(concat);
                            tknService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]),
                                    Integer.parseInt(years[i] + group), positions[k]));
                        }
                    }

                } else {

                    for (int i = 0; i < years.length; i++) {
                        concat = degree + years[i] + years[i] + group + position;
                        unToken = genToken(concat);
                        tknService.create(new TokenDto(unToken, degree, Integer.parseInt(years[i]),
                                Integer.parseInt(years[i] + group), position));
                    }

                }

            }

        } else {

            if (group.equals("All")) {

                if (position.equals("All")) {

                    for (int j = 0; j < groups.length; j++) {
                        for (int k = 0; k < positions.length; k++) {
                            concat = degree + year + year + groups[j] + positions[k];
                            unToken = genToken(concat);
                            tknService.create(new TokenDto(unToken, degree, Integer.parseInt(year),
                                    Integer.parseInt(year + groups[j]), positions[k]));
                        }
                    }

                } else {

                    for (int j = 0; j < groups.length; j++) {
                        concat = degree + year + year + groups[j] + position;
                        unToken = genToken(concat);
                        tknService.create(new TokenDto(unToken, degree, Integer.parseInt(year),
                                Integer.parseInt(year + groups[j]), position));
                    }

                }

            } else {

                if (position.equals("All")) {

                    for (int k = 0; k < positions.length; k++) {
                        concat = degree + year + year + group + positions[k];
                        unToken = genToken(concat);
                        tknService.create(new TokenDto(unToken, degree, Integer.parseInt(year),
                                Integer.parseInt(year + group), positions[k]));
                    }

                } else {

                    concat = degree + year + year + group + position;
                    unToken = genToken(concat);
                    tknService.create(new TokenDto(unToken, degree, Integer.parseInt(year),
                            Integer.parseInt(year + group), position));

                }

            }

        } */
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
