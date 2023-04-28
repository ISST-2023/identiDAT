package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;

public interface CDMemberRepository extends JpaRepository<CDMember, Long> {
    List<CDMember> findByCensusMember(CensusMember censusMember);
}
