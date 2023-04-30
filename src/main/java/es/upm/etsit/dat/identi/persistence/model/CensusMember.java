package es.upm.etsit.dat.identi.persistence.model;

import java.util.Collection;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "CensusMembers")
@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
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

    @Column(length = 200, nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(length = 200, nullable = false, unique = true)
    @NonNull
    private String username;

    @Column(length = 200, nullable = false, unique = true)
    private String personalID;

    @Column(length = 200, nullable = false)
    @NonNull
    private String phone;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="degreeId")
    @NonNull
    private Degree degree;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( 
        name = "censusMembers_roles", 
        joinColumns = @JoinColumn(
          name = "censusmember_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( 
        name = "censusMembers_privileges", 
        joinColumns = @JoinColumn(
          name = "censusmember_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id")) 
    private Collection<Privilege> privileges;
}