package es.upm.etsit.dat.identi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.JD;
import es.upm.etsit.dat.identi.persistence.model.JDFile;

public interface JDFileRepository extends JpaRepository<JDFile, Long>{
    JDFile findByJdAndFilenameAndPath(JD jd, String filename, String path);
    JDFile findByFilename(String filename);
}