package es.upm.etsit.dat.identi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Position;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class DelegateDto {
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @NotNull
    private CensusMember censusMember;

    @NonNull
    @NotNull
    private Position position;

    @NonNull
    @NotNull
    private Degree degree;

    @NonNull
    @NotNull
    private Integer diferentiator;

    @NonNull
    @NotNull
    private Integer year;
}
