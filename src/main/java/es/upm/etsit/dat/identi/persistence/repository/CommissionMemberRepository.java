package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;

public interface CommissionMemberRepository extends JpaRepository<CommissionMember, Long> {
    List<CommissionMember> findByCensusMember(CensusMember censusMember);
    List<CommissionMember> findByCensusMemberAndAcademicYear(CensusMember censusMember, String academicYear);
}
