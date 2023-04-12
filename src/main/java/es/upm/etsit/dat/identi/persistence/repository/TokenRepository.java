package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    public Token findByToken (String token);
    public Token findByDegreeAndPositionAndDiferentiator(Long degreeId, Long positionId, Integer diferentiator);
}
