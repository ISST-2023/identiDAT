package es.upm.etsit.dat.identi.forms;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import es.upm.etsit.dat.identi.persistence.model.CDMember;
import es.upm.etsit.dat.identi.persistence.model.CommissionMember;
import es.upm.etsit.dat.identi.persistence.model.Delegate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PositionForm {
    private Long censusMemberId;
    private List<Delegate> positions;
    private List<CDMember> departments;
    private List<CommissionMember> commissions;
}