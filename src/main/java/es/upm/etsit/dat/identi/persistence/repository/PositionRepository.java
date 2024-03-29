package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
    public Position findByName(String name);
}
