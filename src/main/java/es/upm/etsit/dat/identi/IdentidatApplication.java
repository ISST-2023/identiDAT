package es.upm.etsit.dat.identi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentidatApplication {

	public static void main(String[] args) {
		SpringApplication identidat = new SpringApplication(IdentidatApplication.class);
		identidat.run(args);
	}
}
