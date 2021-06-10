/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import ec.gob.ambiente.sis.model.GenderAdvances;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sigma.projects_gender_info")
@NamedQueries({
    @NamedQuery(name = "ProjectsGenderInfo.findAll", query = "SELECT p FROM ProjectsGenderInfo p")})
public class ProjectsGenderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pgin_id")
    private Integer pginId;
    @Size(max = 200)
    @Column(name = "pgin_other_line")
    private String pginOtherLine;
    @Size(max = 100)
    @Column(name = "pgin_budget")
    private String pginBudget;
    @Size(max = 200)
    @Column(name = "pgin_indicator")
    private String pginIndicator;
    @Size(max = 2147483647)
    @Column(name = "pgin_activities")
    private String pginActivities;
    @Size(max = 2147483647)
    @Column(name = "pgin_results")
    private String pginResults;
    @Column(name = "pgin_status")
    private Boolean pginStatus;
    
    @Getter
    @Setter
    @Column(name = "pgin_results_type")
    private String pginResultsType;
    
    @JoinColumn(name = "cata_id", referencedColumnName = "cata_id")
    @ManyToOne
    private Catalog cataId;
    
    @JoinColumn(name = "proj_id", referencedColumnName = "proj_id")
    @ManyToOne(optional = false)
    private Projects projects;
    
    @Getter
	@Setter
	@OneToMany(mappedBy = "projectsGenderInfo", fetch = FetchType.LAZY)
	private List<GenderAdvances> genderAdvancesList;
    
    @Transient
    private boolean seleccionado;

    public ProjectsGenderInfo() {
    	pginStatus=true;
    }

    public ProjectsGenderInfo(Integer pginId) {
        this.pginId = pginId;
    }

    public Integer getPginId() {
        return pginId;
    }

    public void setPginId(Integer pginId) {
        this.pginId = pginId;
    }

    public String getPginOtherLine() {
        return pginOtherLine;
    }

    public void setPginOtherLine(String pginOtherLine) {
        this.pginOtherLine = pginOtherLine;
    }

    public String getPginBudget() {
        return pginBudget;
    }

    public void setPginBudget(String pginBudget) {
        this.pginBudget = pginBudget;
    }

    public String getPginIndicator() {
        return pginIndicator;
    }

    public void setPginIndicator(String pginIndicator) {
        this.pginIndicator = pginIndicator;
    }
    
    

    public String getPginActivities() {
		return pginActivities;
	}

	public void setPginActivities(String pginActivities) {
		this.pginActivities = pginActivities;
	}

	public String getPginResults() {
		return pginResults;
	}

	public void setPginResults(String pginResults) {
		this.pginResults = pginResults;
	}

	public Boolean getPginStatus() {
        return pginStatus;
    }

    public void setPginStatus(Boolean pginStatus) {
        this.pginStatus = pginStatus;
    }

    public Catalog getCataId() {
        return cataId;
    }

    public void setCataId(Catalog cataId) {
        this.cataId = cataId;
    }

    public Projects getProjId() {
        return projects;
    }

    public void setProjId(Projects projId) {
        this.projects = projId;
    }
    
    

    public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (pginId != null ? pginId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectsGenderInfo)) {
            return false;
        }
        ProjectsGenderInfo other = (ProjectsGenderInfo) object;
        if ((this.pginId == null && other.pginId != null) || (this.pginId != null && !this.pginId.equals(other.pginId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.ambiente.sigma.ejb.entidades.ProjectsGenderInfo[ pginId=" + pginId + " ]";
    }
    
}

