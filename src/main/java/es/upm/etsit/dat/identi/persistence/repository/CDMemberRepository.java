package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CDMember;

public interface CDMemberRepository extends JpaRepository<CDMember, Long> {
    
}
