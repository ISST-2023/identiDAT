package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.ParticipantJD;

public interface ParticipantJDRepository extends JpaRepository<ParticipantJD, Long> {
    
}
