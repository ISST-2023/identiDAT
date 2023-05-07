package es.upm.etsit.dat.identi.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import es.upm.etsit.dat.identi.persistence.model.AssistanceJD;
import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Privilege;
import es.upm.etsit.dat.identi.persistence.model.Role;
import es.upm.etsit.dat.identi.persistence.model.Setting;
import es.upm.etsit.dat.identi.persistence.repository.AssistanceJDRepository;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.PrivilegeRepository;
import es.upm.etsit.dat.identi.persistence.repository.RoleRepository;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements
    ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private CensusMemberRepository cenMemRepo;

  @Autowired
  private RoleRepository roleRepo;

  @Autowired
  private PrivilegeRepository privRepo;

  @Autowired
  private DegreeRepository dgrRepo;

  @Autowired
  private SettingRepository stngRepo;

  @Autowired
  private AssistanceJDRepository assistJDRepo;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if ((stngRepo.findBySettingKey("alreadySetup") != null)
        ? Boolean.parseBoolean(stngRepo.findBySettingKey("alreadySetup").getSettingValue())
        : false)
      return;
    Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
    Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
    Privilege callPrivilege = createPrivilegeIfNotFound("CALL_PRIVILEGE");

    List<Privilege> adminPrivileges = Arrays.asList(
        readPrivilege, writePrivilege);

    List<Privilege> secretaryPrivileges = Arrays.asList(
        readPrivilege, writePrivilege, callPrivilege);

    Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
    Role secretaryRole = createRoleIfNotFound("ROLE_SECRETARY", secretaryPrivileges);

    if (dgrRepo.findByCode("09DA") == null)
      dgrRepo.save(new Degree(Long.valueOf(1), "Delegación de Alumnos de Telecomunicación", "09DA", "DAT"));

    cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(1), "Delegado de Alumnos", "ETSIT-UPM",
        "delegado.alumnos.etsit@upm.es", "delegado.alumnos.etsit", "00000000D", "636980510", dgrRepo.findByCode("09DA"),
        Arrays.asList(adminRole), Arrays.asList(callPrivilege)));
    cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(2), "Secretaría de la Delegación de Alumnos", "ETSIT-UPM",
        "secretaria.dat.etsit@upm.es", "secretaria.dat.etsit", "00000000S", "910671919", dgrRepo.findByCode("09DA"),
        Arrays.asList(adminRole, secretaryRole), null));

    AssistanceJD attends = new AssistanceJD(Long.valueOf(1), "Asiste");
    AssistanceJD misses = new AssistanceJD(Long.valueOf(2), "No asiste");
    AssistanceJD excuses = new AssistanceJD(Long.valueOf(3), "Excusa");
    AssistanceJD expelled = new AssistanceJD(Long.valueOf(4), "Expulsado");

    assistJDRepo.saveAllAndFlush(Arrays.asList(attends, misses, excuses, expelled));

    stngRepo.saveAndFlush(new Setting(1, "alreadySetup", "Primera configuración completada", "true"));
    stngRepo.saveAndFlush(new Setting(2, "filesPath", "Ruta para la subida de ficheros", "uploads"));
    stngRepo.saveAndFlush(new Setting(3, "academicYear", "Curso académico", "2022-23"));

    seedDepartments();
    seedPositions();
    seedDegrees();
    seedCommissions();
    seedMockCensusMembers();
    seedMockPositions();

  }

  @Transactional
  Privilege createPrivilegeIfNotFound(String name) {

    Privilege privilege = privRepo.findByName(name);
    if (privilege == null) {
      privilege = new Privilege(name);
      privRepo.saveAndFlush(privilege);
    }
    return privilege;
  }

  @Transactional
  Role createRoleIfNotFound(
      String name, Collection<Privilege> privileges) {

    Role role = roleRepo.findByName(name);
    if (role == null) {
      role = new Role(name);
      role.setPrivileges(privileges);
      roleRepo.saveAndFlush(role);
    }
    return role;
  }

  @Transactional
  public void seedMockCensusMembers() {
    Degree gitst = dgrRepo.findByCode("09TT");
    Degree gib = dgrRepo.findByCode("09IB");
    Degree gisd = dgrRepo.findByCode("09ID");

    List<CensusMember> censusMembers = new ArrayList<>();

    if (cenMemRepo.findByUsername("e.munoz") == null)
      censusMembers.add(
          new CensusMember("Eduard", "Muñoz Pérez", "e.munoz@alumnos.upm.es", "e.munoz", "00000001T", "600000000",
              gitst));

    if (cenMemRepo.findByUsername("e.carrasco") == null)
      censusMembers.add(
          new CensusMember("Esther", "Carrasco", "e.carrasco@alumnos.upm.es", "e.carrasco", "00000002T", "600000001",
              gitst));

    if (cenMemRepo.findByUsername("v.rubio") == null)
      censusMembers.add(
          new CensusMember("Vicente", "Rubio", "v.rubio@alumnos.upm.es", "v.rubio", "00000003T", "600000002", gib));

    if (cenMemRepo.findByUsername("a.diez") == null)
      censusMembers.add(
          new CensusMember("Ángela", "Diez", "a.diez@alumnos.upm.es", "a.diez", "00000004T", "600000003", gisd));

    if (cenMemRepo.findByUsername("m.rodriguez") == null)
      censusMembers.add(
          new CensusMember("Miriam", "Rodriguez", "m.rodriguez@alumnos.upm.es", "m.rodriguez", "00000005T", "600000004",
              gitst));

    if (cenMemRepo.findByUsername("t.gimenez") == null)
      censusMembers.add(
          new CensusMember("Tomás", "Gimenez", "t.gimenez@alumnos.upm.es", "t.gimenez", "00000006T", "600000005",
              gisd));

    if (cenMemRepo.findByUsername("v.marin") == null)
      censusMembers.add(
          new CensusMember("Vicente", "Marín", "v.marin@alumnos.upm.es", "v.marin", "00000007T", "600000006", gitst));

    if (cenMemRepo.findByUsername("i.alvarez") == null)
      censusMembers.add(
          new CensusMember("Ismael", "Álvarez", "i.alvarez@alumnos.upm.es", "i.alvarez", "00000008T", "600000007",
              gib));

    if (cenMemRepo.findByUsername("s.dominguez") == null)
      censusMembers.add(
          new CensusMember("Sergio", "Dominguez", "s.dominguez@alumnos.upm.es", "s.dominguez", "00000009T", "600000008",
              gitst));

    if (cenMemRepo.findByUsername("a.diaz") == null)
      censusMembers.add(
          new CensusMember("Andrea", "Díaz", "a.diaz@alumnos.upm.es", "a.diaz", "00000010T", "600000009", gitst));

    if (cenMemRepo.findByUsername("r.munoz") == null)
      censusMembers.add(
          new CensusMember("Raúl", "Muñoz", "r.munoz@alumnos.upm.es", "r.munoz", "00000011T", "600000010", gib));

    cenMemRepo.saveAllAndFlush(censusMembers);
  }

  @Autowired
  private DelegateRepository dlgRepo;

  @Autowired
  private CDMemberRepository cdMemRepo;

  @Autowired
  private CommissionMemberRepository cmmMemRepo;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private CommissionRepository cmmRepo;

  @Transactional
  public void seedMockPositions() {
    Position deleGrupo = positionRepository.findByName("Delegado/a de grupo");
    Position deleCurso = positionRepository.findByName("Delegado/a de curso");
    Position deleTitulacion = positionRepository.findByName("Delegado/a de titulación");

    Department dit = departmentRepository.findByAcronym("DIT");
    Department ssr = departmentRepository.findByAcronym("SSR");

    Commission je = cmmRepo.findByName("Junta de Escuela");
    Commission coa = cmmRepo.findByName("Comisión de Ordenación Académica");

    String academicYear = stngRepo.findBySettingKey("academicYear").getSettingValue();

    dlgRepo.deleteAll();
    cdMemRepo.deleteAll();
    cmmMemRepo.deleteAll();

    CensusMember censusMember1 = cenMemRepo.findByUsername("e.munoz");
    dlgRepo.save(new Delegate(censusMember1, deleGrupo, censusMember1.getDegree(), 35, academicYear));
    dlgRepo.save(new Delegate(censusMember1, deleTitulacion, censusMember1.getDegree(), 0, academicYear));

    CensusMember censusMember2 = cenMemRepo.findByUsername("e.carrasco");
    dlgRepo.save(new Delegate(censusMember2, deleGrupo, censusMember2.getDegree(), 23, academicYear));

    CensusMember censusMember3 = cenMemRepo.findByUsername("v.rubio");
    dlgRepo.save(new Delegate(censusMember3, deleGrupo, censusMember3.getDegree(), 33, academicYear));
    dlgRepo.save(new Delegate(censusMember3, deleCurso, censusMember3.getDegree(), 3, academicYear));

    CensusMember censusMember4 = cenMemRepo.findByUsername("a.diez");
    dlgRepo.save(new Delegate(censusMember4, deleGrupo, censusMember4.getDegree(), 22, academicYear));
    cmmMemRepo.save(new CommissionMember(censusMember4, je, academicYear));

    CensusMember censusMember5 = cenMemRepo.findByUsername("m.rodriguez");
    dlgRepo.save(new Delegate(censusMember5, deleGrupo, censusMember5.getDegree(), 15, academicYear));
    dlgRepo.save(new Delegate(censusMember5, deleCurso, censusMember5.getDegree(), 1, academicYear));
    cdMemRepo.save(new CDMember(censusMember5, ssr, academicYear, false));

    CensusMember censusMember6 = cenMemRepo.findByUsername("t.gimenez");
    dlgRepo.save(new Delegate(censusMember6, deleGrupo, censusMember6.getDegree(), 31, academicYear));
    cdMemRepo.save(new CDMember(censusMember6, dit, academicYear, false));

    CensusMember censusMember7 = cenMemRepo.findByUsername("v.marin");
    dlgRepo.save(new Delegate(censusMember7, deleGrupo, censusMember7.getDegree(), 22, academicYear));
    cmmMemRepo.save(new CommissionMember(censusMember7, coa, academicYear));

    CensusMember censusMember8 = cenMemRepo.findByUsername("i.alvarez");
    dlgRepo.save(new Delegate(censusMember8, deleCurso, censusMember8.getDegree(), 1, academicYear));

    CensusMember censusMember9 = cenMemRepo.findByUsername("s.dominguez");
    dlgRepo.save(new Delegate(censusMember9, deleGrupo, censusMember9.getDegree(), 41, academicYear));
    dlgRepo.save(new Delegate(censusMember9, deleCurso, censusMember9.getDegree(), 4, academicYear));

    CensusMember censusMember10 = cenMemRepo.findByUsername("a.diaz");
    dlgRepo.save(new Delegate(censusMember10, deleGrupo, censusMember10.getDegree(), 24, academicYear));

    CensusMember censusMember11 = cenMemRepo.findByUsername("r.munoz");
    dlgRepo.save(new Delegate(censusMember11, deleGrupo, censusMember11.getDegree(), 31, academicYear));

  }

  @Transactional
  public void seedDepartments() {
    Department dit = new Department(Long.valueOf(1), "DIT", "Departamento de Ingeniería de Sistemas Telemáticos");
    departmentRepository.save(dit);

    Department ssr = new Department(Long.valueOf(2), "SSR", "Señales, Sistemas y Radiocomunicaciones");
    departmentRepository.save(ssr);

    Department die = new Department(Long.valueOf(3), "DIE", "Departamento de Ingeniería Electrónica");
    departmentRepository.save(die);

    Department elf = new Department(Long.valueOf(4), "ELF", "Departamento de Electrónica Física");
    departmentRepository.save(elf);

    Department mat = new Department(Long.valueOf(5), "MAT",
        "Matemática aplicada a las tecnologías de la información y las comunicaciones");
    departmentRepository.save(mat);

    Department tfb = new Department(Long.valueOf(6), "TFB",
        "Departamento de Tecnología Fotónica y Bioingeniería");
    departmentRepository.save(tfb);

    Department ior = new Department(Long.valueOf(7), "IOR",
        "Ingeniería de Organización, Administración de Empresas y Estadística");
    departmentRepository.save(ior);

    Department lia = new Department(Long.valueOf(8), "LIA", "Lingüística aplicada a la ciencia y la tecnología");
    departmentRepository.save(lia);
  }

  @Transactional
  public void seedPositions() {
    Position deleGrupo = new Position(Long.valueOf(1), "Delegado/a de grupo");
    positionRepository.save(deleGrupo);

    Position subGrupo = new Position(Long.valueOf(2), "Subdelegado/a de grupo");
    positionRepository.save(subGrupo);

    Position deleCurso = new Position(Long.valueOf(3), "Delegado/a de curso");
    positionRepository.save(deleCurso);

    Position subCurso = new Position(Long.valueOf(4), "Subdelegado/a de curso");
    positionRepository.save(subCurso);

    Position deleTitulacion = new Position(Long.valueOf(5), "Delegado/a de titulación");
    positionRepository.save(deleTitulacion);

    Position subTitulacion = new Position(Long.valueOf(6), "Subdelegado/a de titulación");
    positionRepository.save(subTitulacion);

    Position deleEscuela = new Position(Long.valueOf(7), "Delegado/a de Escuela");
    positionRepository.save(deleEscuela);

    Position subEscuela = new Position(Long.valueOf(8), "Subdelegado/a de Escuela");
    positionRepository.save(subEscuela);

    Position secretario = new Position(Long.valueOf(9), "Secretario/a");
    positionRepository.save(secretario);

    Position tesorero = new Position(Long.valueOf(10), "Tesorero/a");
    positionRepository.save(tesorero);
  }

  @Autowired
  private DegreeRepository degreeRepository;

  @Transactional
  public void seedDegrees() {
    if (degreeRepository.findByCode("09DA") == null)
      degreeRepository
          .save(new Degree(Long.valueOf(1), "Delegación de Alumnos de Telecomunicación", "09DA", "DAT"));

    if (degreeRepository.findByCode("09TT") == null)
      degreeRepository.save(
          new Degree(Long.valueOf(2), "Grado en Ingeniería de Tecnologías y Servicios de Telecomunicación",
              "09TT", "GITST"));

    if (degreeRepository.findByCode("09IB") == null)
      degreeRepository.save(new Degree(Long.valueOf(3), "Grado en Ingeniería Biomédica", "09IB", "GIB"));

    if (degreeRepository.findByCode("09ID") == null)
      degreeRepository
          .save(new Degree(Long.valueOf(4), "Grado en Ingeniería y Sistemas de Datos", "09ID", "GISD"));

    if (degreeRepository.findByCode("09AQ") == null)
      degreeRepository.save(new Degree(Long.valueOf(5), "Máster Universitario en Ingeniería de Telecomunicación",
          "09AQ", "MUIT"));

    if (degreeRepository.findByCode("09AS") == null)
      degreeRepository.save(
          new Degree(Long.valueOf(6), "Máster Universitario en Ingeniería de Redes y Servicios Telemáticos",
              "09AS", "MUIRST"));

    if (degreeRepository.findByCode("09AZ") == null)
      degreeRepository
          .save(new Degree(Long.valueOf(7), "Máster Universitario en Ingeniería de Sistemas Electrónicos",
              "09AZ", "MUISE"));

    if (degreeRepository.findByCode("09AT") == null)
      degreeRepository
          .save(new Degree(Long.valueOf(8), "Máster Universitario en Teoría de la Señal y Comunicaciones",
              "09AT", "MUTSC"));

    if (degreeRepository.findByCode("09AX") == null)
      degreeRepository.save(new Degree(Long.valueOf(9), "Máster Universitario en Energía Solar Fotovoltaica",
          "09AX", "MUESFV"));

    if (degreeRepository.findByCode("09AU") == null)
      degreeRepository
          .save(new Degree(Long.valueOf(10), "Máster Universitario en Ingeniería Biomédica", "09AU", "MUIB"));
  }

  @Transactional
  public void seedCommissions() {
    if (cmmRepo.findByName("Junta de Escuela") == null)
      cmmRepo.save(new Commission(Long.valueOf(1), "Junta de Escuela"));

    if (cmmRepo.findByName("Claustro Universitario") == null)
      cmmRepo.save(new Commission(Long.valueOf(2), "Claustro Universitario"));

    if (cmmRepo.findByName("Comisión de Gobierno") == null)
      cmmRepo.save(new Commission(Long.valueOf(3), "Comisión de Gobierno"));

    if (cmmRepo.findByName("Comisión Electoral de Centro") == null)
      cmmRepo.save(new Commission(Long.valueOf(4), "Comisión Electoral de Centro"));

    if (cmmRepo.findByName("Comisión de Ordenación Académica") == null)
      cmmRepo.save(new Commission(Long.valueOf(5), "Comisión de Ordenación Académica"));

    if (cmmRepo.findByName("Junta de Representantes UPM") == null)
      cmmRepo.save(new Commission(Long.valueOf(6), "Junta de Representantes UPM"));

    if (cmmRepo.findByName("Junta de Delegados UPM") == null)
      cmmRepo.save(new Commission(Long.valueOf(7), "Junta de Delegados UPM"));

    if (cmmRepo.findByName("Comisión Electoral Central") == null)
      cmmRepo.save(new Commission(Long.valueOf(8), "Comisión Electoral Central"));

    if (cmmRepo.findByName("Comisión de Calidad") == null)
      cmmRepo.save(new Commission(Long.valueOf(9), "Comisión de Calidad"));
  }

}