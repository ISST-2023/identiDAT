package es.upm.etsit.dat.identi.forms;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import es.upm.etsit.dat.identi.persistence.model.Position;
import es.upm.etsit.dat.identi.persistence.model.Department;
import es.upm.etsit.dat.identi.persistence.model.Commission;

@Getter @Setter
public class PositionForm {
    private Long censusMemberId;
    private List<Position> positions;
    private List<Department> deparments;
    private List<Commission> commissions;
}