package es.upm.etsit.dat.identi.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DepartmentsForm {
    private Long censusMemId;
    private Long departmentId;
    private String year;
}