package ec.gob.ambiente.sigma.model;

import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;

/**
 * Entity implementation class for Entity: Projects
 *
 */
@Entity
@Table(name = "projects", schema = "sigma")
@NamedQueries({
	@NamedQuery(name = Projects.CARGAR_TODOS_LOS_PROYECTOS,query = "SELECT P FROM Projects P WHERE P.projStatus=true")
	
})
public class Projects {

	
	
	public static final String CARGAR_TODOS_LOS_PROYECTOS="cargarTodosLosProyectos"; 

	@Getter
	@Setter
	@Column(name = "proj_id")
	@Id
	@SequenceGenerator(name = "PROJECTS_GENERATOR", initialValue = 1, sequenceName = "projects_proj_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTS_GENERATOR")
	private Integer projId;
	
	@Getter
	@Setter
	@Column(name = "proj_title")
	private String projTitle;
	
	@Getter
	@Setter
	@Column(name = "proj_status")
	private boolean projStatus;
	
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsCobenefits> projectsCobenefitsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSafeguardsList;
	
	@Getter
	@Setter
	@JoinColumn(name = "part_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Partners partners;
	
	@Getter
	@Setter
	@JoinColumn(name = "acpl_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ActionPlans actionPlans;
   
}
