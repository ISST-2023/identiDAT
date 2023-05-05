package es.upm.etsit.dat.identi.persistence.model;

import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "JDFiles", uniqueConstraints = {@UniqueConstraint(columnNames = {"jd_id", "filename", "path"})})
@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class JDFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name="jd_id")
    @NonNull
    private JD jd;
    
    @Column(length = 200, nullable = false)
    @NonNull
    private String filename;

    @Column(length = 500, nullable = false)
    @NonNull
    private String path;

    @Column(length = 200, nullable = false)
    @NonNull
    private String contentType;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private java.sql.Timestamp uploaded;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private java.sql.Timestamp updated;
}