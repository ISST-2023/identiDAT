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
@Table(name = "ParticipantsJD")
public class ParticipantJD {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="jdId")
    private JD jdId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="censusId")
    private CensusMember censusId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="assistanceId")
    private AssistanceJD assistanceId;

    public ParticipantJD() {
    }

    public ParticipantJD(Long id, JD jdId, CensusMember censusId, AssistanceJD assistanceId) {
        this.id = id;
        this.jdId = jdId;
        this.censusId = censusId;
        this.assistanceId = assistanceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JD getJdId() {
        return jdId;
    }

    public void setJdId(JD jdId) {
        this.jdId = jdId;
    }

    public CensusMember getCensusId() {
        return censusId;
    }

    public void setCensusId(CensusMember censusId) {
        this.censusId = censusId;
    }

    public AssistanceJD getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(AssistanceJD assistanceId) {
        this.assistanceId = assistanceId;
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
        ParticipantJD other = (ParticipantJD) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ParticipantJD [id=" + id + ", jdId=" + jdId + ", censusId=" + censusId + ", assistanceId="
                + assistanceId + "]";
    }

    
}
