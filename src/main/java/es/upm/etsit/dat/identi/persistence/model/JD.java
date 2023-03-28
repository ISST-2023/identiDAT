package es.upm.etsit.dat.identi.persistence.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "JD")
public class JD {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private java.sql.Timestamp year;

    @Column(nullable = false)
    private Boolean ordinary;

    @Column(length = 200, nullable = false)
    private String place;

    public JD() {
    }

    public JD(Timestamp year, Boolean ordinary, String place) {
        this.year = year;
        this.ordinary = ordinary;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.sql.Timestamp getYear() {
        return year;
    }

    public void setYear(java.sql.Timestamp year) {
        this.year = year;
    }

    public Boolean getOrdinary() {
        return ordinary;
    }

    public void setOrdinary(Boolean ordinary) {
        this.ordinary = ordinary;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
        JD other = (JD) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "JD [id=" + id + ", year=" + year + ", ordinary=" + ordinary + ", place=" + place + "]";
    }

    
}
