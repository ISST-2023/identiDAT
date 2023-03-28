package es.upm.etsit.dat.identi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMemberRepositoryTest {

    @Autowired
    private CensusMemberRepository repo;

    @Test
    public void saveCensusMember() {
        CensusMember alvaro = new CensusMember("Álvaro", "Pérez", "alvaro@alumnos.upm.es", "666666666", 1, true);
        CensusMember perico = new CensusMember("Perico", "Pérez", "perico@alumnos.upm.es", "777777777", 2, false);

        repo.save(alvaro);
        repo.save(perico);

        repo.flush();

        assertNotNull(alvaro);
        assertNotNull(alvaro.getId());

        assertEquals("Álvaro", alvaro.getName());
        
        assertEquals(2, repo.findAll().size());

    }

}
