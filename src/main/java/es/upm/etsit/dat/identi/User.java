package es.upm.etsit.dat.identi;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter @Setter
public class User implements Serializable {
    private String username;
    private Boolean isAdmin;
}
