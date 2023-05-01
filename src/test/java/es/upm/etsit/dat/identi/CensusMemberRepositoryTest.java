package es.upm.etsit.dat.identi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMemberRepositoryTest {

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Test
    public void saveCensusMember() {
        CensusMember alvaro = new CensusMember(Long.valueOf(66), "Álvaro", "Rosado", "alvaro@alumnos.upm.es", "alvaro", "00000000Z", "666666666", dgrRepo.findByCode("09DA"), null, null); 
        CensusMember perico = new CensusMember(Long.valueOf(77), "Perico", "Pérez", "perico@alumnos.upm.es", "perico", "00000000A", "777777777", dgrRepo.findByCode("09DA"), null, null);

        cenMemRepo.save(alvaro);
        cenMemRepo.save(perico);

        cenMemRepo.flush();

        assertNotNull(alvaro);
        assertNotNull(alvaro.getId());

        assertEquals("Álvaro", alvaro.getName());
    }
}
