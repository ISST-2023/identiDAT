package es.upm.etsit.dat.identi.persistence.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {

    public String save(byte[] content, String path) throws Exception {
        Path newFile = Paths.get(path);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath()
                .toString();
    }

    public Optional<FileSystemResource> retrieve(String path) {
        FileSystemResource file;
        try {
            file =  new FileSystemResource(Paths.get(path));   
        } catch (Exception e) {
            file = null;
        }
        return Optional.ofNullable(file);
    }

    public Boolean delete(String path) {
        try {
            Files.delete(Paths.get(path));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}