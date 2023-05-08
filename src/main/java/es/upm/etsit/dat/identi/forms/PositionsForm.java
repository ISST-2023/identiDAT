package es.upm.etsit.dat.identi.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PositionsForm {
    private Long cenMemberId;
    private Long positionId;
    private String degreeCode;
    private Integer diferentiator;
    private String year;
}