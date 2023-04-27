package es.upm.etsit.dat.identi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Commission;
import es.upm.etsit.dat.identi.persistence.model.CommissionToken;

public interface CommissionTokenRepository extends JpaRepository<CommissionToken, Long> {
    public CommissionToken findByToken (String token);
    public List<CommissionToken> findByCommission(Commission commission);
}
