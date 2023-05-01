package es.upm.etsit.dat.identi.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.upm.etsit.dat.identi.forms.JDForm;
import es.upm.etsit.dat.identi.persistence.model.JD;
import es.upm.etsit.dat.identi.persistence.repository.JDRepository;


@Controller
public class JDController {
    @Autowired
    private JDRepository jdRepo;

    @GetMapping("/admin/jd")
    public String jd(Model model) {
        model.addAttribute("JDForm", new JDForm());
        return "admin/jd/index";
    }

    @PostMapping("/admin/jd")
    public String saveJd(@ModelAttribute("JDForm") JDForm jdForm, Model model){
        String goodDate = jdForm.getDate().replace('T', ' ') + ":00";
        JD newJD = new JD(
            Timestamp.valueOf(goodDate),
            Boolean.valueOf(jdForm.getOrdinary()),
            jdForm.getPlace());
        jdRepo.save(newJD);
        return "admin/jd/index";
    }



    
    
}
