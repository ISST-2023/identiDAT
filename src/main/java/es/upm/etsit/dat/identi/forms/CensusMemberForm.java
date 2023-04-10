package es.upm.etsit.dat.identi.forms;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class CensusMemberForm implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String personalID;
    private Integer phone;
    private String degree;
    private String position;
    private Boolean agreement;

    public CensusMemberForm (String name, String surname, String email, String degree, String position) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.degree = degree;
        this.position = position;
    }
}
