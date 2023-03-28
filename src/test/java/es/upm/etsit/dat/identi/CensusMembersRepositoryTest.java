package es.upm.etsit.dat.identi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.upm.etsit.dat.identi.persistence.model.CensusMembers;
import es.upm.etsit.dat.identi.persistence.repository.CensusMembersRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMembersRepositoryTest {

    @Autowired
    private CensusMembersRepository repo;

    @Test
    public void saveCensusMembers() {
        CensusMembers alvaro = new CensusMembers("Álvaro", "Pérez", "alvaro@alumnos.upm.es", "666666666", 1, true);
        CensusMembers perico = new CensusMembers("Perico", "Pérez", "perico@alumnos.upm.es", "777777777", 2, false);

        repo.save(alvaro);
        repo.save(perico);

        repo.flush();

        System.out.println(repo.findAll());
        assertEquals(2, repo.findAll().size());

    }

}
