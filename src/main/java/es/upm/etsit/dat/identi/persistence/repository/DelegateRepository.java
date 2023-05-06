package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;
import es.upm.etsit.dat.identi.persistence.model.Position;

public interface DelegateRepository extends JpaRepository<Delegate, Long> {
    Delegate findByPositionAndDiferentiatorAndAcademicYear(Position position, Integer diferentiator, String academicYear);
    List<Delegate> findByCensusMember(CensusMember censusMember);
    List<Delegate> findByCensusMemberAndAcademicYear(CensusMember censusMember, String academicYear);
}
