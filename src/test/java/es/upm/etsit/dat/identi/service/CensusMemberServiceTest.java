package es.upm.etsit.dat.identi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CensusMemberServiceTest {
    
    @Autowired
    private CensusMemberServiceImpl cenMemService;

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Test
    public void constraintsValidationExceptionTest() {
        assertThrows(ConstraintViolationException.class, ()-> {cenMemService.create(new CensusMemberDto("Álvaro", "", "alvaro2@alumnos.upm.es", "666666666A", 666666666, "GISD"));});
        assertThrows(ConstraintViolationException.class, ()-> {cenMemService.create(new CensusMemberDto("Perico", "Pérez", "p.perez", "777777777B", 777777777, "GIB"));});
    }

    @Test
    public void saveCensusMemberOk() {
        CensusMemberDto cenMemDto = cenMemService.create(new CensusMemberDto("Álvaro", "Pérez", "alvaro3@alumnos.upm.es", "00000000Z", 666666666, "GITST"));
        assertNotNull(cenMemDto);
        assertEquals("Álvaro", cenMemDto.getName());
        assertNotNull(cenMemDto.getId());

        cenMemRepo.deleteById(cenMemDto.getId());
    }
}