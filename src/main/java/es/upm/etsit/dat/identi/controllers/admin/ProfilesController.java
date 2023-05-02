package es.upm.etsit.dat.identi.controllers.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.upm.etsit.dat.identi.FieldValidator;
import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.forms.PositionForm;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;

@Controller
public class ProfilesController {

  @Autowired
  private CensusMemberService cenMemService;

  @Autowired
  private CensusMemberRepository cenMemRepo;

  @Autowired
  private DegreeRepository dgrRepo;

  @Autowired
  private PositionRepository posRepo;

  @Autowired
  private DepartmentRepository depRepo;

  @Autowired
  private CommissionRepository commRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private FieldValidator fieldValidator;

  @GetMapping("/admin/census")
  public String census(Model model) {
    model.addAttribute("censusMembers", cenMemService.getAll());
    return "admin/census/index";
  }

  @GetMapping("/admin/census/new")
  public String newForm(Model model) {
    CensusMemberForm cenMemForm = new CensusMemberForm();
    model.addAttribute("censusMemberForm", cenMemForm);
    model.addAttribute("degrees", dgrRepo.findAll());
    return "admin/census/new";
  }

  @PostMapping("/admin/census/new")
  public String register(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
    CensusMemberDto censusMemberDto;
    CensusMember censusMember = cenMemRepo.findByEmail(cenMemForm.getEmail());

    if (censusMember != null) {
      model.addAttribute("error",
          "Este usuario ya existe dentro de la base de datos, si desea modificar alguno de sus atributos por favor intentelo desde la página del censo.");
      return "error";
    }

    Boolean errorFound = false;

    if (!fieldValidator.validateEmail(cenMemForm.getEmail())) {
      errorFound = true;
      model.addAttribute("emailError", "No se ha introducido un email válido.");
    }

    if (fieldValidator.emailExists(cenMemForm.getEmail())) {
      errorFound = true;
      model.addAttribute("emailError", "Ya existe un usuario registrado con ese email.");
    }

    if (!fieldValidator.validatePhone(cenMemForm.getPhone())) {
      errorFound = true;
      model.addAttribute("phoneError", "No se ha introducido un número de teléfono válido.");
    }

    if (!fieldValidator.validateID(cenMemForm.getPersonalID())) {
      errorFound = true;
      model.addAttribute("personalIDError", "El DNI/NIE introducido no es válido.");
    }

    if (fieldValidator.IDExists(cenMemForm.getPersonalID())) {
      errorFound = true;
      model.addAttribute("personalIDError", "Un usuario con ese DNI/NIE ya está registrado.");
    }

    if (dgrRepo.findByCode(cenMemForm.getDegreeCode()) == null) {
      errorFound = true;
      model.addAttribute("degreeError", "La titulación especificada no se encuentra registrada.");
    }

    if (errorFound) {
      model.addAttribute("censusMemberForm", cenMemForm);
      model.addAttribute("degrees", dgrRepo.findAll());
      return "admin/census/new";
    }

    if (censusMember == null) {
      censusMemberDto = new CensusMemberDto(
          cenMemForm.getName(),
          cenMemForm.getSurname(),
          cenMemForm.getEmail(),
          cenMemForm.getEmail().split("@")[0],
          cenMemForm.getPersonalID(),
          cenMemForm.getPhone(),
          dgrRepo.findByCode(cenMemForm.getDegreeCode()));

      censusMember = modelMapper.map(censusMemberDto, CensusMember.class);
    }

    cenMemRepo.saveAndFlush(censusMember);
    return "redirect:/admin/census";
  }

  @GetMapping("/admin/census/edit/{id}")
  public ModelAndView editForm(@ModelAttribute("id") Long id) {
    if (id == 1 || id == 2) return new ModelAndView("redirect:/admin/census");
    var params = new HashMap<String, Object>();
    CensusMemberForm form = new CensusMemberForm();
    CensusMemberDto cenMem = cenMemService.get(id);
    form.setCensusMemberId(cenMem.getId());
    form.setName(cenMem.getName());
    form.setSurname(cenMem.getSurname());
    form.setEmail(cenMem.getEmail());
    form.setPersonalID(cenMem.getPersonalID());
    form.setPhone(cenMem.getPhone());
    form.setDegreeCode(cenMem.getDegree().getCode());

    params.put("degrees", dgrRepo.findAll());
    params.put("censusMemberForm", form);

    return new ModelAndView("admin/census/edit", params);
  }

  @PostMapping("/admin/census/edit")
  public String edit(@ModelAttribute("censusMemberForm") CensusMemberForm cenMemForm, Model model) {
    if (cenMemForm.getCensusMemberId() == 1 || cenMemForm.getCensusMemberId() == 2) return "redirect:/admin/census";
    Optional<CensusMember> censusMemberCandidate = cenMemRepo.findById(cenMemForm.getCensusMemberId());
    CensusMember censusMember;
    boolean errorFound = false;

    if (censusMemberCandidate.isPresent()) {
      censusMember = censusMemberCandidate.get();

      if (!fieldValidator.validateEmail(cenMemForm.getEmail())) {
        errorFound = true;
        model.addAttribute("emailError", "No se ha introducido un email válido.");
      }

      if (fieldValidator.emailBelongsToCensusMember(cenMemForm.getEmail(), censusMember)) {
        errorFound = true;
        model.addAttribute("emailError", "Ya existe un usuario registrado con ese email.");
      }

      if (!fieldValidator.validatePhone(cenMemForm.getPhone())) {
        errorFound = true;
        model.addAttribute("phoneError", "No se ha introducido un número de teléfono válido.");
      }

      if (!fieldValidator.validateID(cenMemForm.getPersonalID())) {
        errorFound = true;
        model.addAttribute("personalIDError", "El DNI/NIE introducido no es válido.");
      }

      if (!fieldValidator.IDBelongsToCensusMember(cenMemForm.getPersonalID(), censusMember)) {
        errorFound = true;
        model.addAttribute("personalIDError", "Un usuario con ese DNI/NIE ya está registrado.");
      }

      if (dgrRepo.findByCode(cenMemForm.getDegreeCode()) == null) {
        errorFound = true;
        model.addAttribute("degreeError", "La titulación especificada no se encuentra registrada.");
      }

      if (errorFound) {
        model.addAttribute("censusMemberForm", cenMemForm);
        model.addAttribute("degrees", dgrRepo.findAll());
        return "admin/census/edit";
      }

      censusMember.setName(cenMemForm.getName());
      censusMember.setSurname(cenMemForm.getSurname());
      censusMember.setEmail(cenMemForm.getEmail());
      censusMember.setPersonalID(cenMemForm.getPersonalID());
      censusMember.setPhone(cenMemForm.getPhone());
      censusMember.setDegree(dgrRepo.findByCode(cenMemForm.getDegreeCode()));

      cenMemRepo.saveAndFlush(censusMember);
    } else {
      model.addAttribute("error", "El usuario que intentas editar no existe.");
      return "error";
    }

    return "redirect:/admin/census";
  }

  @PostMapping("/admin/census/delete/{id}")
  public ModelAndView delete(@ModelAttribute("id") Long id) {
    cenMemService.delete(id);
    return new ModelAndView("redirect:/admin/census");
  }

  @Autowired
  DelegateRepository dlgRepo;

  @Autowired
  CDMemberRepository cdMemRepo;

  @Autowired
  CommissionMemberRepository cmmMemRepo;

  @GetMapping("/admin/census/positions/{id}")
  public String editPositions(@ModelAttribute("id") Long id, Model model) {
    CensusMember censusMember;
    Optional<CensusMember> censusMemberCandidate = cenMemRepo.findById(id);
    if (!censusMemberCandidate.isPresent()) {
      model.addAttribute("error", "El usuario que intentas editar no existe.");
      return "error";
    } else censusMember = censusMemberCandidate.get();

    List<Delegate> delegatePositions = dlgRepo.findByCensusMember(censusMember);
    List<CDMember> cds = cdMemRepo.findByCensusMember(censusMember);
    List<CommissionMember> commissions = cmmMemRepo.findByCensusMember(censusMember);
    
    PositionForm form = new PositionForm(id, delegatePositions, cds, commissions);
    CensusMemberDto cenMem = cenMemService.get(id);
    form.setCensusMemberId(cenMem.getId());
    model.addAttribute("positions", posRepo.findAll());
    model.addAttribute("departments", depRepo.findAll());
    model.addAttribute("commissions", commRepo.findAll());

    model.addAttribute("form", form);

    return "admin/census/positions";
  }

  @PostMapping("/admin/census/positions")
  public String registerPositions(@ModelAttribute("form") CensusMemberForm cenMemForm, Model model) {
    Optional<CensusMember> censusMember = cenMemRepo.findById(cenMemForm.getCensusMemberId());

    if (censusMember.isPresent()) {
      CensusMember member = censusMember.get();
      member.setName(cenMemForm.getName());
      member.setSurname(cenMemForm.getSurname());
      member.setEmail(cenMemForm.getEmail());
      member.setPersonalID(cenMemForm.getPersonalID());
      member.setPhone(cenMemForm.getPhone());
      member.setDegree(dgrRepo.findByAcronym(cenMemForm.getDegreeAcronym()));

      cenMemRepo.saveAndFlush(member);
      cenMemRepo.flush();
    }

    return "redirect:/admin/census";
  }
}
