package es.upm.etsit.dat.identi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.upm.etsit.dat.identi.model.CensusMembers;
import es.upm.etsit.dat.identi.repository.ICensusMembersJpaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMembersJpaRepositoryTest {

    @Autowired
    private ICensusMembersJpaRepository repo;

    @Test
    public void saveCensusMember() {
        CensusMembers alvaro = new CensusMembers("Álvaro", "Pérez", "alvaro@alumnos.upm.es", "666666666", 1, 1, true);

        repo.save(alvaro);

        repo.flush();

        assertEquals(1, repo.findAll().size());
    }

}
