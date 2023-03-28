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
@Table(name = "Delegates")
public class Delegate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="censusId")
    private CensusMember censusId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="positionId")
    private CensusMember positionId;
    
    @Column(nullable = false)
    private Integer year;

    public Delegate() {
    }

    public Delegate(CensusMember censusId, CensusMember positionId, Integer year) {
        this.censusId = censusId;
        this.positionId = positionId;
        this.year = year;
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

    public CensusMember getPositionId() {
        return positionId;
    }

    public void setPositionId(CensusMember positionId) {
        this.positionId = positionId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
        Delegate other = (Delegate) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Delegate [id=" + id + ", censusId=" + censusId + ", positionId=" + positionId + ", year=" + year + "]";
    }
    
    
}
