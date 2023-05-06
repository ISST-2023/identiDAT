package es.upm.etsit.dat.identi.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @ToString
public class JDForm {
    private Long id;

    @NonNull
    private String date;

    @NonNull
    private Boolean ordinary;

    @NonNull
    private String place;
}

