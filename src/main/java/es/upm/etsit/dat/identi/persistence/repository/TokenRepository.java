package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    public Token findByToken (String token);
    public Token findByDegreeAndPositionAndDiferentiator(Degree degree, Position position, Integer diferentiator);
}
