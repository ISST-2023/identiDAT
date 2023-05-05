package es.upm.etsit.dat.identi.persistence.model;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "jd")
@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class JD {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private java.sql.Timestamp date;

    @Column(nullable = false)
    @NonNull
    private Boolean ordinary;

    @Column(length = 200, nullable = false)
    @NonNull
    private String place;

    @OneToMany(mappedBy="jd", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<JDFile> files;
}