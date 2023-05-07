package es.upm.etsit.dat.identi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CDToken;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionToken;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.CDMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CDTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionRepository;
import es.upm.etsit.dat.identi.persistence.repository.CommissionTokenRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DelegateRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartmentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private DelegateRepository dlgRepo;

    @Autowired
    private CDMemberRepository cdMemRepo;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private CommissionMemberRepository cmmMemRepo;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CommissionRepository cmmRepo;

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
        seedTokens();
    }
}
