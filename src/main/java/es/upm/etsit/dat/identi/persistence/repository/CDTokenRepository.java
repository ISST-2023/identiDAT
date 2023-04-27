package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.CDToken;

public interface CDTokenRepository extends JpaRepository<CDToken, Long> {
    public CDToken findByToken (String token);
    public List<CDToken> findByDepartment(Department department);
}
