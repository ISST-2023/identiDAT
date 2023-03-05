package es.upm.etsit.dat.identi;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentidatApplication {

	public static void main(String[] args) {
		SpringApplication identidat = new SpringApplication(IdentidatApplication.class);
		identidat.setDefaultProperties(Collections.singletonMap("server.port", "25565"));
		identidat.run(args);
	}
}
