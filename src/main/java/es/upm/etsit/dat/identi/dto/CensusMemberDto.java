package es.upm.etsit.dat.identi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
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

    @NonNull
    @NotBlank
    @Size(max = 200)
    private String personalID;

    @NonNull
    @NotBlank
    @Size(max = 200)
    private Integer phone;

    @NonNull
    @NotNull
    private String degree;

    @NonNull
    private Boolean admin = false;

}
