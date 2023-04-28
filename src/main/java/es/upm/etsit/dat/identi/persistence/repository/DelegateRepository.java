package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Position;

public interface DelegateRepository extends JpaRepository<Delegate, Long> {
    Delegate findByPositionAndDiferentiatorAndYear(Position position, Integer diferentiator, Integer year);
    List<Delegate> findByCensusId(CensusMember censusMember);
}
