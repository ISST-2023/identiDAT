package es.upm.etsit.dat.identi.persistence.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CDMembers")
public class CDMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="censusId")
    private CensusMember censusId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="departamentId")
    private Departament departamentId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Boolean head;

    public CDMember() {
    }

    public CDMember(CensusMember censusId, Departament departamentId, Integer year, Boolean head) {
        this.censusId = censusId;
        this.departamentId = departamentId;
        this.year = year;
        this.head = head;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CensusMember getCensusId() {
        return censusId;
    }

    public void setCensusId(CensusMember censusId) {
        this.censusId = censusId;
    }

    public Departament getDepartamentId() {
        return departamentId;
    }

    public void setDepartamentId(Departament departamentId) {
        this.departamentId = departamentId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CDMember other = (CDMember) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CDMember [id=" + id + ", censusId=" + censusId + ", departamentId=" + departamentId + ", year=" + year
                + ", head=" + head + "]";
    }

    
}
