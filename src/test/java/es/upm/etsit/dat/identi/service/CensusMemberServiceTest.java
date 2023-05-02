package es.upm.etsit.dat.identi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import jakarta.validation.ConstraintViolationException;

@Disabled("Deshabilitado hasta que se actualice.")
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMemberServiceTest {
    
    @Autowired
    private CensusMemberServiceImpl cenMemService;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Test
    public void constraintsValidationExceptionTest() {
        assertThrows(ConstraintViolationException.class, ()-> {cenMemService.create(new CensusMemberDto("Álvaro", "", "alvaro2@alumnos.upm.es", "alvaro2", "666666666A", "666666666", dgrRepo.findByCode("09ID")));});
        assertThrows(ConstraintViolationException.class, ()-> {cenMemService.create(new CensusMemberDto("Perico", "Pérez", "p.perez", "p.perez", "777777777B", "777777777", dgrRepo.findByCode("09IB")));});
    }

    @Test
    public void saveCensusMemberOk() {
        CensusMemberDto cenMemDto = cenMemService.create(new CensusMemberDto("Álvaro", "Pérez", "alvaro3@alumnos.upm.es", "alvaro3", "00000000Z", "666666666", dgrRepo.findByCode("09TT")));
        assertNotNull(cenMemDto);
        assertEquals("Álvaro", cenMemDto.getName());
        assertNotNull(cenMemDto.getId());

        cenMemRepo.deleteById(cenMemDto.getId());
    }
}
