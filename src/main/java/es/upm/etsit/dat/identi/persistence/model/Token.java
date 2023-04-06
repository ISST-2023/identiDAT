package es.upm.etsit.dat.identi.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Tokens")
@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(length = 200, nullable = false, unique = true)
    @NonNull
    private String token;

    @Column(length = 200, nullable = false)
    @NonNull
    private String degree;

    @Column(nullable = false)
    @NonNull
    private Integer year;

    @Column(nullable = false)
    @NonNull
    private Integer grupo;

    @Column(length = 200, nullable = false)
    @NonNull
    private String position;

}
