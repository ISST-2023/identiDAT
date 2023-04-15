package es.upm.etsit.dat.identi.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Departaments")
@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class Departament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 200, nullable = false, unique = true)
    @NonNull
    private String acronym;

    @Column(length = 200, nullable = false)
    @NonNull
    private String name;
}