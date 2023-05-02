package es.upm.etsit.dat.identi;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

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

    @Autowired
    private PositionRepository positionRepository;

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

    @Autowired
    private CommissionRepository cmmRepo;

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

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CDTokenRepository cdTokenRepository;

    @Autowired
    private CommissionTokenRepository commissionTokenRepository;

    public void seedTokens() {
        Degree gitst = degreeRepository.findByCode("09TT");
        Degree gib = degreeRepository.findByCode("09IB");
        Degree gisd = degreeRepository.findByCode("09ID");

        Position deleGrupo = positionRepository.findByName("Delegado/a de grupo");
        Position deleCurso = positionRepository.findByName("Delegado/a de curso");
        Position deleTitulacion = positionRepository.findByName("Delegado/a de titulación");

        if (tokenRepository.findByDegreeAndPositionAndDiferentiator(gitst, deleGrupo, 14) == null)
            tokenRepository.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, deleGrupo, 14));

        if (tokenRepository.findByDegreeAndPositionAndDiferentiator(gitst, deleTitulacion, 0) == null)
            tokenRepository.save(new Token(RandomStringUtils.randomAlphanumeric(64), gitst, deleTitulacion, 0));

        if (tokenRepository.findByDegreeAndPositionAndDiferentiator(gib, deleCurso, 2) == null)
            tokenRepository.save(new Token(RandomStringUtils.randomAlphanumeric(64), gib, deleCurso, 2));

        if (tokenRepository.findByDegreeAndPositionAndDiferentiator(gisd, deleTitulacion, 0) == null)
            tokenRepository.save(new Token(RandomStringUtils.randomAlphanumeric(64), gisd, deleTitulacion, 0));

        Department dit = departmentRepository.findByAcronym("DIT");
        Department ssr = departmentRepository.findByAcronym("SSR");

        if (cdTokenRepository.findByDepartment(dit).size() == 0)
            cdTokenRepository.save(new CDToken(RandomStringUtils.randomAlphanumeric(64), dit));

        if (cdTokenRepository.findByDepartment(ssr).size() == 0)
            cdTokenRepository.save(new CDToken(RandomStringUtils.randomAlphanumeric(64), ssr));

        Commission je = cmmRepo.findByName("Junta de Escuela");
        Commission coa = cmmRepo.findByName("Comisión de Ordenación Académica");

        if (commissionTokenRepository.findByCommission(je).size() == 0)
            commissionTokenRepository.save(new CommissionToken(RandomStringUtils.randomAlphanumeric(64), je));

        if (commissionTokenRepository.findByCommission(coa).size() == 0)
            commissionTokenRepository.save(new CommissionToken(RandomStringUtils.randomAlphanumeric(64), coa));

    }

    @Override
    public void run(String... args) throws Exception {
        seedDepartments();
        seedPositions();
        seedDegrees();
        seedCommissions();
        seedTokens();
    }
}
