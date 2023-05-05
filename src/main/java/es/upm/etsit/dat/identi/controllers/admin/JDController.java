package es.upm.etsit.dat.identi.controllers.admin;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/admin/jd")
    public String jd(Model model) {
        model.addAttribute("JDs", jdRepo.findAll());
        model.addAttribute("JDForm", new JDForm());
        return "admin/jd/index";
    }

    @PostMapping("/admin/jd")
    public String saveJd(@ModelAttribute("JDForm") JDForm jdForm, Model model) {
        String goodDate = jdForm.getDate().replace('T', ' ') + ":00";
        JD newJD = new JD(
                Timestamp.valueOf(goodDate),
                Boolean.valueOf(jdForm.getOrdinary()),
                jdForm.getPlace());
        jdRepo.save(newJD);
        return "redirect:/admin/jd";
    }

    @GetMapping("/admin/jd/{id}/files")
    public String jdFiles(@ModelAttribute("id") Long id, Model model) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            model.addAttribute("error", "La sesión especificada no existe.");
            return "redirect:/error";
        } else
            jd = jdCandidate.get();

        model.addAttribute("jd", jd);
        model.addAttribute("jdFileForm", new JDFileForm());
        return "admin/jd/session";
    }

    @PostMapping("/admin/jd/{id}/files")
    @ResponseBody
    public String jdUploadFile(@ModelAttribute("id") Long id, @ModelAttribute("jdFileForm") JDFileForm jdFileForm,
            Model model, HttpServletResponse response) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "La sesión especificada no existe.";
        } else
            jd = jdCandidate.get();

        String rootDir = stngRepo.findBySettingKey("filesPath").getSettingValue();
        String academicYear = stngRepo.findBySettingKey("academicYear").getSettingValue();
        String filePath = String.format("%s/%s/jd/%s/%s", rootDir, academicYear,
                jd.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")),
                jdFileForm.getFile().getOriginalFilename());

        if (jdFileRepo.findByFilename(jdFileForm.getName()) != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "Un fichero con ese nombre ya existe.";
        }

        if (jdFileRepo.findByJdAndFilenameAndPath(jd, jdFileForm.getName(),
                filePath) != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "El fichero ya existe.";
        }

        try {
            if (fsRepo.save(jdFileForm.getFile().getBytes(), filePath))
                jdFileRepo
                        .saveAndFlush(new JDFile(jd, jdFileForm.getName(), filePath,
                                jdFileForm.getFile().getContentType(),
                                Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now())));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "Ha ocurrido un error.";
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return "";
    }

    @GetMapping("/admin/jd/{jdId}/files/{id}")
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

    @PostMapping("/admin/jd/{jdId}/files/{id}/delete")
    @ResponseBody
    public String jdDeleteFile(@ModelAttribute("jdId") Long jdId, @ModelAttribute("id") Long id,
            Model model, HttpServletResponse response) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(jdId);
        if (!jdCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "La sesión especificada no existe.";
        } else
            jd = jdCandidate.get();

        JDFile jdFile;
        Optional<JDFile> jdFileCandidate = jdFileRepo.findById(id);
        if (!jdFileCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "El fichero especificado no existe.";
        } else
            jdFile = jdFileCandidate.get();

        if (!jdFile.getJd().equals(jd)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "";
        }

        response.setStatus(HttpServletResponse.SC_ACCEPTED);

        try {
            if (fsRepo.delete(jdFile.getPath())) {
                jdFileRepo.delete(jdFile);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return "";
    }

    @PostMapping("/admin/jd/{jdId}/files/{id}/update")
    @ResponseBody
    public String jdUpdateFile(@ModelAttribute("jdId") Long jdId, @ModelAttribute("id") Long id,
            @ModelAttribute("jdFileForm") JDFileForm jdFileForm,
            Model model, HttpServletResponse response) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(jdId);
        if (!jdCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "La sesión especificada no existe.";
        } else
            jd = jdCandidate.get();

        JDFile jdFile;
        Optional<JDFile> jdFileCandidate = jdFileRepo.findById(id);
        if (!jdFileCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "El fichero especificado no existe.";
        } else
            jdFile = jdFileCandidate.get();

        if (!jdFile.getJd().equals(jd)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "";
        }

        String rootDir = stngRepo.findBySettingKey("filesPath").getSettingValue();
        String academicYear = stngRepo.findBySettingKey("academicYear").getSettingValue();
        String filePath = String.format("%s/%s/jd/%s/%s", rootDir, academicYear,
                jd.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")),
                jdFileForm.getFile().getOriginalFilename());

        try {
            if (fsRepo.delete(jdFile.getPath())) {
                if (fsRepo.save(jdFileForm.getFile().getBytes(), filePath)) {
                    jdFile.setPath(filePath);
                    jdFile.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                    jdFile.setContentType(jdFileForm.getFile().getContentType());
                    jdFileRepo.saveAndFlush(jdFile);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "Ha ocurrido un error.";
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return "";
    }
}