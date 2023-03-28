package es.upm.etsit.dat.identi.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;

public interface CensusMemberRepository extends JpaRepository<CensusMember, Long> {
    CensusMember findByEmail(String email);

}