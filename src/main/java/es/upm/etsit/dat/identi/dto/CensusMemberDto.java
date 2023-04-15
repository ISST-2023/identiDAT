package es.upm.etsit.dat.identi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class CensusMemberDto {
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @NotBlank
    @Size(max = 200)
    private String name;

    @NonNull
    @NotBlank
    @Size(max = 200)
    private String surname;

    @NonNull
    @NotBlank
    @Size(max = 200)
    @Email
    private String email;

    @Size(max = 200)
    private String username;

    @NonNull
    @NotBlank
    @Size(max = 200)
    @NotNull
    private String personalID;

    @NonNull
    private Integer phone;

    @NonNull
    @NotNull
    private Degree degree;

    @NonNull
    private Boolean admin = false;
}