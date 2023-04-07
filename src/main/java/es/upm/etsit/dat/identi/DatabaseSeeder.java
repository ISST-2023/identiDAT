package es.upm.etsit.dat.identi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Departament;
import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.DepartamentRepository;
import es.upm.etsit.dat.identi.persistence.repository.PositionRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
 
    @Autowired
    private DepartamentRepository departamentRepository;

    public void seedDepartments() {
        Departament dit = new Departament(Long.valueOf(1), "DIT", "Departamento de Ingeniería de Sistemas Telemáticos");
        departamentRepository.save(dit);

        Departament ssr = new Departament(Long.valueOf(2), "SSR", "Señales, Sistemas y Radiocomunicaciones");
        departamentRepository.save(ssr);

        Departament die = new Departament(Long.valueOf(3), "DIE", "Departamento de Ingeniería Electrónica");
        departamentRepository.save(die);

        Departament elf = new Departament(Long.valueOf(4), "ELF", "Departamento de Electrónica Física");
        departamentRepository.save(elf);

        Departament mat = new Departament(Long.valueOf(5), "MAT", "Matemática aplicada a las tecnologías de la información y las comunicaciones");
        departamentRepository.save(mat);

        Departament tfb = new Departament(Long.valueOf(6), "TFB", "Departamento de Tecnología Fotónica y Bioingeniería");
        departamentRepository.save(tfb);

        Departament ior = new Departament(Long.valueOf(7), "IOR", "Ingeniería de Organización, Administración de Empresas y Estadística");
        departamentRepository.save(ior);

        Departament lia = new Departament(Long.valueOf(8), "LIA", "Lingüística aplicada a la ciencia y la tecnología");
        departamentRepository.save(lia);
    }

    @Autowired
    private PositionRepository positionRepository;

    public void seedPositions() {
        Position deleGrupo = new Position(Long.valueOf(1), "Delegado/a de grupo");
        positionRepository.save(deleGrupo);

        Position subGrupo = new Position(Long.valueOf(2), "Subdelegado/a de grupo");
        positionRepository.save(subGrupo);       
        
        Position deleCurso = new Position(Long.valueOf(3), "Delegado/a de curso");
        positionRepository.save(deleCurso);

        Position subCurso = new Position(Long.valueOf(4), "Subdelegado/a de curso");
        positionRepository.save(subCurso);

        Position deleTitulacion = new Position(Long.valueOf(5), "Delegado/a de titulación");
        positionRepository.save(deleTitulacion);

        Position subTitulacion = new Position(Long.valueOf(6), "Subdelegado/a de titulación");
        positionRepository.save(subTitulacion);

        Position deleEscuela = new Position(Long.valueOf(7), "Delegado/a de Escuela");
        positionRepository.save(deleEscuela);

        Position subEscuela = new Position(Long.valueOf(8), "Subdelegado/a de Escuela");
        positionRepository.save(subEscuela);

        Position secretario = new Position(Long.valueOf(9), "Secretario/a");
        positionRepository.save(secretario);

        Position tesorero = new Position(Long.valueOf(10), "Tesorero/a");
        positionRepository.save(tesorero);        
    }

    @Autowired
    private DegreeRepository degreeRepository;

    public void seedDegrees() {
        degreeRepository.save(new Degree("Grado en Ingeniería de Tecnologías y Servicios de Telecomunicación", "09TT", "GITST"));
        degreeRepository.save(new Degree("Grado en Ingeniería Biomédica", "09IB", "GIB"));
        degreeRepository.save(new Degree("Grado en Ingeniería y Sistemas de Datos", "09ID", "GISD"));
        degreeRepository.save(new Degree("Máster Universitario en Ingeniería de Telecomunicación", "09AQ", "MUIT"));
        degreeRepository.save(new Degree("Máster Universitario en Ingeniería de Redes y Servicios Telemáticos", "09AS", "MUIRST"));
        degreeRepository.save(new Degree("Máster Universitario en Ingeniería de Sistemas Electrónicos", "09AZ", "MUISE"));
        degreeRepository.save(new Degree("Máster Universitario en Teoría de la Señal y Comunicaciones", "09AT", "MUTSC"));
        degreeRepository.save(new Degree("Máster Universitario en Energía Solar Fotovoltaica", "09AX", "MUESFV"));
        degreeRepository.save(new Degree("Máster Universitario en Ingeniería Biomédica", "09AU", "MUIB"));
    }

    @Override
    public void run(String... args) throws Exception {
        seedDepartments();
        seedPositions();
        seedDegrees();
    }
}

