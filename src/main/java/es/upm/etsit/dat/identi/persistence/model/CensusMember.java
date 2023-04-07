package es.upm.etsit.dat.identi.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CensusMembers")
@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class CensusMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 200, nullable = false)
    @NonNull
    private String name;

    @Column(length = 200, nullable = false)
    @NonNull
    private String surname;

    @Column(length = 200, nullable = false, unique=true)
    @NonNull
    private String email;

    @Column(length = 200, nullable = false, unique=true)
    private String personalID;

    @Column(length = 200, nullable = false)
    @NonNull
    private Integer phone;

    @Column(nullable = false)
    @NonNull
    private String degree;

    @Column(nullable = false)
    private Boolean admin;

    public CensusMember(String name, String surname, String email, String personalID, Integer phone, String degree) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.personalID = personalID;
        this.phone = phone;
        this.degree = degree;
        this.admin = false;
    }
}