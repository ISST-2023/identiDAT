package es.upm.etsit.dat.identi.controllers.admin;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.etsit.dat.identi.forms.JDFileForm;
import es.upm.etsit.dat.identi.forms.JDForm;
import es.upm.etsit.dat.identi.persistence.model.AssistanceJD;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.JD;
import es.upm.etsit.dat.identi.persistence.model.JDFile;
import es.upm.etsit.dat.identi.persistence.model.ParticipantJD;
import es.upm.etsit.dat.identi.persistence.repository.AssistanceJDRepository;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.FileSystemRepository;
import es.upm.etsit.dat.identi.persistence.repository.JDFileRepository;
import es.upm.etsit.dat.identi.persistence.repository.JDRepository;
import es.upm.etsit.dat.identi.persistence.repository.ParticipantJDRepository;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JDControllerAdmin {
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
        model.addAttribute("JDs",
                jdRepo.findByAcademicYear(stngRepo.findBySettingKey("academicYear").getSettingValue()));
        model.addAttribute("JDForm", new JDForm());
        return "admin/jd/index";
    }

    @GetMapping("/admin/jd/all")
    public String allJD(Model model) {
        model.addAttribute("JDs", jdRepo.findAll());
        model.addAttribute("JDForm", new JDForm());
        return "admin/jd/index";
    }

    @PostMapping("/admin/jd")
    public String saveJD(@ModelAttribute("JDForm") JDForm jdForm, Model model) {
        String goodDate = jdForm.getDate().replace('T', ' ') + ":00";
        JD newJD = new JD(
                Timestamp.valueOf(goodDate),
                Boolean.valueOf(jdForm.getOrdinary()),
                jdForm.getPlace(),
                stngRepo.findBySettingKey("academicYear").getSettingValue());
        jdRepo.save(newJD);
        return "redirect:/admin/jd";
    }

    @PostMapping("/admin/jd/{id}")
    public String updateJD(@ModelAttribute("id") Long id, @ModelAttribute("JDForm") JDForm jdForm, Model model) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            model.addAttribute("error", "La sesión especificada no existe.");
            return "redirect:/error";
        } else
            jd = jdCandidate.get();

        String goodDate = jdForm.getDate().replace('T', ' ') + ":00";
        jd.setDate(Timestamp.valueOf(goodDate));
        jd.setOrdinary(Boolean.valueOf(jdForm.getOrdinary()));
        jd.setPlace(jdForm.getPlace());
        jdRepo.saveAndFlush(jd);
        return "redirect:/admin/jd/" + String.valueOf(id) + "/files";
    }

    @GetMapping("/admin/jd/{id}/files")
    public String jdFiles(@ModelAttribute("id") Long id, Model model, HttpServletRequest request) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            model.addAttribute("error", "La sesión especificada no existe.");
            return "redirect:/error";
        } else
            jd = jdCandidate.get();

        model.addAttribute("jd", jd);
        model.addAttribute("_csrf", request.getAttribute("_csrf"));
        model.addAttribute("JDForm",
                new JDForm(jd.getDate().toLocalDateTime().toString(), jd.getOrdinary(), jd.getPlace()));
        model.addAttribute("jdFileForm", new JDFileForm());

        return "admin/jd/files";
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

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private DelegateRepository dlgRepo;

    @Autowired
    private CDMemberRepository cdMemRepo;

    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Autowired
    private AssistanceJDRepository assistJdRepo;

    @Autowired
    private ParticipantJDRepository partJdRepo;

    @GetMapping("/admin/jd/{id}/assistance")
    public String jdAssistance(@ModelAttribute("id") Long id, Model model, HttpServletRequest request) {
        List<CensusMember> members = new ArrayList<>();
        List<CensusMember> guests = new ArrayList<>();
        List<String> allowedDelegates = Arrays.asList("Delegado/a de Curso", "Subdelegado/a de Curso",
                "Delegado/a de Titulación", "Subdelegado/a de Titulación", "Delegado/a de Escuela", "Secretario/a");

        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            model.addAttribute("error", "La sesión especificada no existe.");
            return "redirect:/error";
        } else
            jd = jdCandidate.get();

        List<CensusMember> census = cenMemRepo.findAll();

        for (CensusMember censusMember : census) {
            if (censusMember.getId() == 1 || censusMember.getId() == 2)
                continue;
            List<Delegate> delegatePositions = dlgRepo.findByCensusMember(censusMember);
            List<CDMember> cdMemberPositions = cdMemRepo.findByCensusMember(censusMember);
            List<CommissionMember> cmmMemberPositions = cmmMemRepo.findByCensusMember(censusMember);

            Boolean allowedDelegate = false;
            Boolean allowedCommission = false;

            for (Delegate delegate : delegatePositions) {
                if (allowedDelegates.contains(delegate.getPosition().getName()))
                    allowedDelegate = true;
            }

            for (CommissionMember cmmMember : cmmMemberPositions) {
                if (cmmMember.getCommission().getName() == "Junta de Escuela")
                    allowedCommission = true;
            }

            if (cdMemberPositions.size() > 0 || allowedDelegate || allowedCommission)
                members.add(censusMember);
            else
                guests.add(censusMember);
        }

        List<ParticipantJD> participantJdObjects = partJdRepo.findByJd(jd);
        Map<Long, Long> participants = new HashMap<>();

        for (ParticipantJD participantJd : participantJdObjects)
            participants.put(participantJd.getCensusMember().getId(), participantJd.getAssistance().getId());

        model.addAttribute("jd", jd);
        model.addAttribute("members", members);
        model.addAttribute("guests", guests);
        model.addAttribute("participants", participants);
        model.addAttribute("_csrf", request.getAttribute("_csrf"));
        return "admin/jd/assistance";
    }

    @PostMapping("/admin/jd/{id}/assistance/{cenMemId}/{status}")
    @ResponseBody
    public String jdAssistanceRegister(@ModelAttribute("id") Long id, @ModelAttribute("cenMemId") Long cenMemId,
            @ModelAttribute("status") Long status, Model model, HttpServletResponse response) {
        JD jd;
        Optional<JD> jdCandidate = jdRepo.findById(id);
        if (!jdCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "La sesión especificada no existe.";
        } else
            jd = jdCandidate.get();

        CensusMember censusMember;
        Optional<CensusMember> censusMemberCandidate = cenMemRepo.findById(cenMemId);
        if (!censusMemberCandidate.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "El usuario especificado no existe.";
        } else
            censusMember = censusMemberCandidate.get();

        if (status != 0) {
            AssistanceJD assistanceJd;
            Optional<AssistanceJD> assistanceJdCandidate = assistJdRepo.findById(status);
            if (!assistanceJdCandidate.isPresent()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "No existe un estado previo para este usuario.";
            } else
                assistanceJd = assistanceJdCandidate.get();

            ParticipantJD currentStatus = partJdRepo.findByCensusMemberAndJd(censusMember, jd);
            if (currentStatus != null)
                currentStatus.setAssistance(assistanceJd);
            else
                currentStatus = new ParticipantJD(jd, censusMember, assistanceJd);
            partJdRepo.save(currentStatus);
        } else {
            ParticipantJD currentStatus = partJdRepo.findByCensusMemberAndJd(censusMember, jd);
            if (currentStatus != null)
                partJdRepo.delete(currentStatus);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "";
    }
}