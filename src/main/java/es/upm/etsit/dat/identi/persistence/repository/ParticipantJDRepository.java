package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.JD;
import es.upm.etsit.dat.identi.persistence.model.ParticipantJD;
import java.util.List;


public interface ParticipantJDRepository extends JpaRepository<ParticipantJD, Long> {
    List<ParticipantJD> findByJd(JD jd);
    ParticipantJD findByCensusMemberAndJd(CensusMember censusMember, JD jd);
}
