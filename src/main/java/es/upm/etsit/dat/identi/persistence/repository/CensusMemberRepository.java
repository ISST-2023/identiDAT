package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Degree;

public interface CensusMemberRepository extends JpaRepository<CensusMember, Long> {
    CensusMember findByEmail(String email);
    CensusMember findByUsername(String username);
    CensusMember findByPersonalID(String personalID);
    List<CensusMember> findByDegree(Degree degree);
}