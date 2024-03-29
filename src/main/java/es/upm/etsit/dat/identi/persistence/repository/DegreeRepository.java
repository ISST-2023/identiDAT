package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Degree;

public interface DegreeRepository extends JpaRepository<Degree, Long>{
    Degree findByCode(String code);
    Degree findByAcronym(String acronym);
}
