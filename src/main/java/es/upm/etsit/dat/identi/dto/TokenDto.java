package es.upm.etsit.dat.identi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class TokenDto {
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @NotBlank
    @Size(max = 200)
    private String token;

    @NonNull
    @NotBlank
    private Long degree;

    @NonNull
    @NotNull
    private Integer diferentiator;

    @NonNull
    @NotBlank
    private Long position;

}
