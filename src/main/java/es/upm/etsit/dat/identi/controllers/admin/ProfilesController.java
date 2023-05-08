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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.upm.etsit.dat.identi.FieldValidator;
import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.forms.CensusMemberForm;
import es.upm.etsit.dat.identi.forms.PositionForm;
import es.upm.etsit.dat.identi.forms.PositionsForm;
import es.upm.etsit.dat.identi.forms.DepartmentsForm;
import es.upm.etsit.dat.identi.forms.CommissionsForm;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Role;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.RoleRepository;
import es.upm.etsit.dat.identi.service.CensusMemberService;
import jakarta.servlet.http.HttpServletResponse;

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

  @Autowired
  RoleRepository roleRepo;

  @PostMapping("/admin/census/addrole/{id}")
  public String addRole(@ModelAttribute("id") Long id, Model model) {
    if (id == 1 || id == 2) return "";
    Optional<CensusMember> censusMemberCandidate = cenMemRepo.findById(id);
    CensusMember censusMember;
    Long roleId = Long.valueOf(1);

    if (censusMemberCandidate.isPresent()) {
      censusMember = censusMemberCandidate.get();
      //censusMember.getRoles().add(roleRepo.findById(roleId).getName()); //no funciona getName();
      //censusMember.getprivileges().add(privilegeRepository.getById());

      cenMemRepo.saveAndFlush(censusMember);
    } else {
      model.addAttribute("error", "El usuario que intentas editar no existe.");
      return "error";
    }

    return "";
  }

  @PostMapping("/admin/census/removerole/{id}")
  public String removeRole(@ModelAttribute("id") Long id, Model model) {
    if (id == 1 || id == 2) return "";
    Optional<CensusMember> censusMemberCandidate = cenMemRepo.findById(id);
    CensusMember censusMember;
    Long roleId = Long.valueOf(1);

    if (censusMemberCandidate.isPresent()) {
      censusMember = censusMemberCandidate.get();
      Role roleToRemove = censusMember.getRoles().stream().filter(role -> role.getName().equals("ROLE_ADMIN")).findFirst().orElse(null); // Buscas el Role que quieres remover en la colección de roles del CensusMember
      if (roleToRemove != null) {
        censusMember.getRoles().remove(roleToRemove); // Remueves el Role de la colección de roles del CensusMember
        cenMemRepo.saveAndFlush(censusMember);
        //censusMember.getPrivileges().remove(); //remover los privilegios correspondientes
      } else {
        model.addAttribute("error", "El usuario no tiene el rol que intentas remover.");
        return "error";
      }
      
    } else {
      model.addAttribute("error", "El usuario que intentas editar no existe.");
      return "error";
    }

    return "";
  }

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
    
    PositionForm formP = new PositionForm(id, delegatePositions, cds, commissions);
    PositionsForm posForm = new PositionsForm();
    DepartmentsForm depForm = new DepartmentsForm();
    CommissionsForm comForm = new CommissionsForm();
    CensusMemberDto cenMem = cenMemService.get(id);

    formP.setCensusMemberId(cenMem.getId());
    posForm.setCenMemberId(cenMem.getId());
    depForm.setCensusMemId(cenMem.getId());
    comForm.setCenMemId(cenMem.getId());

    model.addAttribute("positionsL", posRepo.findAll());
    model.addAttribute("departmentsL", depRepo.findAll());
    model.addAttribute("commissionsL", commRepo.findAll());
    model.addAttribute("degrees", dgrRepo.findAll());

    model.addAttribute("form", formP);
    model.addAttribute("posForm", posForm);
    model.addAttribute("depForm", depForm);
    model.addAttribute("comForm", comForm);

    return "admin/census/positions";
  }

  @PostMapping("/admin/census/positions/{id}/newposition")
  @ResponseBody
  public String registerPositions(@ModelAttribute("posForm") PositionsForm form, @ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    CensusMember censusMember = cenMemRepo.findById(form.getCenMemberId())
      .orElseThrow(() -> new RuntimeException("Census member not found"));

    Position position = posRepo.findById(form.getPositionId())
      .orElseThrow(() -> new RuntimeException("Position not found"));

    Delegate delegate = new Delegate();

    delegate.setCensusMember(censusMember);
    delegate.setPosition(position);
    delegate.setDegree(dgrRepo.findByCode(form.getDegreeCode()));
    delegate.setDiferentiator(222);
    delegate.setAcademicYear(form.getYear());

    dlgRepo.saveAndFlush(delegate);
    dlgRepo.flush();

    return "";
  }

  @PostMapping("/admin/census/positions/{id}/newdepartment")
  @ResponseBody
  public String registerDepartments(@ModelAttribute("depForm") DepartmentsForm form, @ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    CensusMember censusMember = cenMemRepo.findById(form.getCensusMemId())
      .orElseThrow(() -> new RuntimeException("Census member not found"));
    Department department = depRepo.findById(form.getDepartmentId())
      .orElseThrow(() -> new RuntimeException("Census member not found"));

    CDMember cdMem = new CDMember();//censusMember.getId(), form.getDepartmentId(), form.getYear, (head)

    cdMem.setCensusMember(censusMember);
    cdMem.setDepartment(department);
    cdMem.setAcademicYear(form.getYear());
    cdMem.setHead(true);                  //Preguntar

    cdMemRepo.saveAndFlush(cdMem);
    cdMemRepo.flush();
    
    return "";
  }

  @PostMapping("/admin/census/positions/{id}/newcommission")
  @ResponseBody
  public String registerCommissions(@ModelAttribute("comForm") CommissionsForm form, @ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    CensusMember censusMember = cenMemRepo.findById(form.getCenMemId())
      .orElseThrow(() -> new RuntimeException("Census member not found"));
    Commission comm = commRepo.findById(form.getCommissionId())
      .orElseThrow(() -> new RuntimeException("Census member not found"));
    
    CommissionMember comMem = new CommissionMember();//censusMember.getId(), form.getCommissionId(), form.getYear()
    
    comMem.setCensusMember(censusMember);
    comMem.setCommission(comm);
    comMem.setAcademicYear(form.getYear());

    cmmMemRepo.saveAndFlush(comMem);
    cmmMemRepo.flush();
       
    return "";
  }

  @PostMapping("/admin/census/positions/{id}/deleteposition")
  @ResponseBody
  public String deletePosition(@ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    
    dlgRepo.deleteById(id);
    
    return "";
  }

  @PostMapping("/admin/census/positions/{id}/deletedepartment")
  @ResponseBody
  public String deleteDepartment(@ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    
    cdMemRepo.deleteById(id);
    
    return "";
  }

  @PostMapping("/admin/census/positions/{id}/deletecommission")
  @ResponseBody
  public String deleteCommission(@ModelAttribute("id") Long id, Model model, HttpServletResponse response) {
    
    cmmMemRepo.deleteById(id);
    
    return "";
  }
}
