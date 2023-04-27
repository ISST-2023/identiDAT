package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Department;

public interface DepartamentRepository extends JpaRepository<Department, Long> {
    Department findByAcronym(String acronym);
}
