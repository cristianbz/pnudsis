package ec.gob.ambiente.sis.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Projects
 *
 */
@Entity
@Table(name = "projects", schema = "sis")
@NamedQueries({
	@NamedQuery(name = Projects.CARGAR_TODOS_LOS_PROYECTOS,query = "SELECT P FROM Projects P")
	
})
public class Projects {

	
	
	public static final String CARGAR_TODOS_LOS_PROYECTOS="cargarTodosLosProyectos"; 

	@Getter
	@Setter
	@Column(name = "proj_id")
	@Id
	@SequenceGenerator(name = "PROJECTS_GENERATOR", initialValue = 1, sequenceName = "projects_proj_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTS_GENERATOR")
	private Integer projId;
	
	@Getter
	@Setter
	@Column(name = "proj_name")
	private String projName;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsSafeguards> projectsSaveguardsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSafeguardsList;
   
}
