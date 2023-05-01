package es.upm.etsit.dat.identi.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class JDForm {
    private Long id;
    private String date;
    private Boolean ordinary;
    private String place;
}

