package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.JD;

public interface JDRepository extends JpaRepository<JD, Long> {
    
}
