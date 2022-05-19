/**
@autor proamazonia [Christian Báez]  2 ago. 2021

**/
package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import ec.gob.ambiente.sigma.model.Projects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sis.projects_gender_info")

public class ProjectsGenderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Getter
    @Setter
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pgin_id")
    private Integer pginId;
    
    @Getter
    @Setter
    @Size(max = 200)
    @Column(name = "pgin_other_line")
    private String pginOtherLine;
    
    @Getter
    @Setter
    @Column(name = "pgin_budget")
    private double pginBudget;
    
    
    @Getter
    @Setter
    @Size(max = 800)
    @Column(name = "pgin_associated_results")
    private String pginAssociatedResults;
    
    @Getter
    @Setter    
    @Column(name = "pgin_components")
    private String pginComponents;
        
    @Getter
    @Setter
    @Size(max = 1)
    @Column(name = "pgin_results_type")
    private String pginResultsType;
    
    @Getter
    @Setter
    @Column(name = "pgin_status")
    private Boolean pginStatus;
    
    @Getter
    @Setter
    @Column(name = "pspa_id")
    private Integer pspaId;
    
    @Getter
    @Setter
    @Size(max = 250)
    @Column(name = "pgin_creator_user")
    private String pginCreatorUser;
    
    @Getter
    @Setter    
    @Column(name = "pgin_creation_date")
    private Date pginCreationDate;
    
    @Getter
    @Setter
    @Size(max = 250)
    @Column(name = "pgin_update_user")
    private String pginUpdateUser;

    @Getter
    @Setter    
    @Column(name = "pgin_update_date")
    private Date pginUpdateDate;

    @Getter
    @Setter    
    @Column(name = "pgin_dba_observation")
    private String pginDbaObservation;

    
    @Transient
    @Getter
    @Setter
    private String pginDescripcionLineaAccion;
    
    @Transient
    @Getter
    @Setter
    private String componentesGenero;
    
    @Transient
    @Getter
    @Setter
    private String[] pginObjectivesSelected;
    
	@Getter
	@Setter
	@Transient
	private Integer[] pginObjectivesSelectedAux;
	
    @Getter
    @Setter
    @Transient
    private String pginAssociatedResultsAux;
    
    @Getter
    @Setter
    @JoinColumn(name = "cata_id", referencedColumnName = "cata_id")
    @ManyToOne
    private Catalogs cataId;
    
    @Getter
    @Setter
    @JoinColumn(name = "proj_id", referencedColumnName = "proj_id")
    @ManyToOne(optional = false)
    private Projects projects;
    
    @Getter
	@Setter
	@OneToMany(mappedBy = "projectsGenderInfo", fetch = FetchType.EAGER)
	private List<ProjectGenderIndicator> projectGenderIndicatorList;
        

    
    @Transient
    @Getter
	@Setter
    private boolean seleccionado;

    public ProjectsGenderInfo() {
    	pginStatus=true;
    }

    public ProjectsGenderInfo(Integer pginId) {
        this.pginId = pginId;
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


