package es.upm.etsit.dat.identi.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.etsit.dat.identi.forms.JDFileForm;
import es.upm.etsit.dat.identi.forms.JDForm;
import es.upm.etsit.dat.identi.persistence.model.JD;
import es.upm.etsit.dat.identi.persistence.model.JDFile;
import es.upm.etsit.dat.identi.persistence.repository.FileSystemRepository;
import es.upm.etsit.dat.identi.persistence.repository.JDFileRepository;
import es.upm.etsit.dat.identi.persistence.repository.JDRepository;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JDController {
    @Autowired
    private JDRepository jdRepo;

    @Autowired
    private JDFileRepository jdFileRepo;

    @Autowired
    private SettingRepository stngRepo;

    @Autowired
    private FileSystemRepository fsRepo;

    @GetMapping("/jd")
    public String jd(Model model) {
        model.addAttribute("JDs", jdRepo.findAll());
        model.addAttribute("JDForm", new JDForm());
        return "jd";
    }

    @GetMapping("/jd/{id}/files")
    public String jdFiles(@ModelAttribute("id") Long id, Model model) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            model.addAttribute("error", "La sesión especificada no existe.");
            return "redirect:/error";
        } else
            jd = jdCandidate.get();

        model.addAttribute("jd", jd);
        model.addAttribute("JDForm", new JDForm(jd.getDate().toLocalDateTime().toString(), jd.getOrdinary(), jd.getPlace()));
        model.addAttribute("jdFileForm", new JDFileForm());
        
        return "jd_session";
    }

    @GetMapping("/jd/{jdId}/files/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> jdRetrieveFile(@ModelAttribute("jdId") Long jdId, @ModelAttribute("id") Long id,
            Model model, HttpServletResponse response) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(jdId);
        if (!jdCandidate.isPresent()) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpServletResponse.SC_NOT_FOUND);
        } else
            jd = jdCandidate.get();

        JDFile jdFile;
        Optional<JDFile> jdFileCandidate = jdFileRepo.findById(id);
        if (!jdFileCandidate.isPresent()) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpServletResponse.SC_NOT_FOUND);
        } else
            jdFile = jdFileCandidate.get();

        if (!jdFile.getJd().equals(jd)) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpServletResponse.SC_BAD_REQUEST);
        }

        Optional<FileSystemResource> fileResourceCandidate = fsRepo.retrieve(jdFile.getPath());
        FileSystemResource fileResource;
        if (fileResourceCandidate.isPresent()) {
            fileResource = fileResourceCandidate.get();
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(jdFile.getContentType()));
                return new ResponseEntity<>(fileResource.getContentAsByteArray(), headers, HttpServletResponse.SC_OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, new HttpHeaders(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else
            return new ResponseEntity<>(null, new HttpHeaders(), HttpServletResponse.SC_NOT_FOUND);
    }
}