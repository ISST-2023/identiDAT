package es.upm.etsit.dat.identi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.upm.etsit.dat.identi.persistence.model.Departament;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.repository.DepartamentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
 
    @Autowired
    private DepartamentRepository departamentRepository;

    public void seedDepartments() {
        Departament dit = new Departament(1, "DIT", "Departamento de Ingeniería de Sistemas Telemáticos");
        departamentRepository.save(dit);

        Departament ssr = new Departament(2, "SSR", "Señales, Sistemas y Radiocomunicaciones");
        departamentRepository.save(ssr);

        Departament die = new Departament(3, "DIE", "Departamento de Ingeniería Electrónica");
        departamentRepository.save(die);

        Departament elf = new Departament(4, "ELF", "Departamento de Electrónica Física");
        departamentRepository.save(elf);

        Departament mat = new Departament(5, "MAT", "Matemática aplicada a las tecnologías de la información y las comunicaciones");
        departamentRepository.save(mat);

        Departament tfb = new Departament(6, "TFB", "Departamento de Tecnología Fotónica y Bioingeniería");
        departamentRepository.save(tfb);

        Departament ior = new Departament(7, "IOR", "Ingeniería de Organización, Administración de Empresas y Estadística");
        departamentRepository.save(ior);

        Departament lia = new Departament(8, "LIA", "Lingüística aplicada a la ciencia y la tecnología");
        departamentRepository.save(lia);
    }

    @Autowired
    private PositionRepository positionRepository;

    public void seedPositions() {
        Position deleClase = new Position(1, "Delegado/a de clase");
        positionRepository.save(deleClase);

        Position subClase = new Position(2, "Subdelegado/a de clase");
        positionRepository.save(subClase);       
        
        Position deleCurso = new Position(3, "Delegado/a de curso");
        positionRepository.save(deleCurso);

        Position subCurso = new Position(4, "Subdelegado/a de curso");
        positionRepository.save(subCurso);

        Position deleTitulacion = new Position(5, "Delegado/a de titulación");
        positionRepository.save(deleTitulacion);

        Position subTitulacion = new Position(6, "Subdelegado/a de titulación");
        positionRepository.save(subTitulacion);

        Position deleEscuela = new Position(7, "Delegado/a de Escuela");
        positionRepository.save(deleEscuela);

        Position subEscuela = new Position(8, "Subdelegado/a de Escuela");
        positionRepository.save(subEscuela);

        Position secretario = new Position(9, "Secretario/a");
        positionRepository.save(secretario);

        Position tesorero = new Position(10, "Tesorero/a");
        positionRepository.save(tesorero);        
    }

    @Override
    public void run(String... args) throws Exception {
        seedDepartments();
        seedPositions();
    }
}

