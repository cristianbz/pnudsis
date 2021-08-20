package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import javax.persistence.*;

import ec.gob.ambiente.suia.model.GeographicalLocations;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsEspecificObjetives
 *
 */
@Entity
@Table(name = "projects_specific_objectives", schema = "sigma")
@NamedQueries({
	@NamedQuery(name = "ps",query = "SELECT PSO FROM ProjectsSpecificObjectives PSO WHERE PSO.psobStatus = TRUE AND PSO.projects.projId=1 AND PSO.projects.projType=''")
	
})

public class ProjectsSpecificObjectives implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "psob_id")
    private Integer psobId;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
	
	@Getter
	@Setter
	@Column(name = "psob_description")
	private String psobDescription;
   
	@Getter
	@Setter
	@Column(name = "psob_status")
	private boolean psobStatus;
	
	@Getter
	@Setter
    @JoinColumn(name = "gelo_id", referencedColumnName = "gelo_id")
    @ManyToOne
    private GeographicalLocations geloId;
}
