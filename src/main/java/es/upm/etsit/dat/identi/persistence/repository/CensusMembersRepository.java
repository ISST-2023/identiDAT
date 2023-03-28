package es.upm.etsit.dat.identi.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.CensusMembers;

public interface CensusMembersRepository extends JpaRepository<CensusMembers, Integer> {
    CensusMembers findByEmail(String email);

}