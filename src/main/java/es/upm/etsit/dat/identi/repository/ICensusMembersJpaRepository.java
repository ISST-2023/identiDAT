package es.upm.etsit.dat.identi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.model.CensusMembers;

public interface ICensusMembersJpaRepository extends JpaRepository<CensusMembers, Long> {
    CensusMembers findByEmail(String email);
}
