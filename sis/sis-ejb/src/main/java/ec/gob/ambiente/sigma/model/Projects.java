package ec.gob.ambiente.sigma.model;

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

import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Projects
 *
 */
@Entity
@Table(name = "projects", schema = "sigma")
@NamedQueries({
	
	@NamedQuery(name = Projects.CARGAR_TODOS_LOS_PROYECTOS,query = "SELECT P FROM Projects P WHERE P.projStatus=true"),
	@NamedQuery(name = Projects.CARGAR_SALVAGUARDAS_POR_COBENEFICIO,query = "SELECT PS FROM Projects P  INNER JOIN P.projectsCobenefitsList PC INNER JOIN PC.projectsSafeguardsList PS WHERE P.projId=:codigoProyecto"),
	@NamedQuery(name = Projects.CARGAR_SALVAGUARDAS_POR_RIESGO,query = "SELECT PS FROM Projects P  INNER JOIN P.projectsRisksList PR INNER JOIN PR.projectsSafeguardsList PS WHERE P.projId =:codigoProyecto")
})
public class Projects {

	
	
	public static final String CARGAR_TODOS_LOS_PROYECTOS="cargarTodosLosProyectos"; 
	public static final String CARGAR_SALVAGUARDAS_POR_COBENEFICIO="cargarSalvaguardasPorCobeneficio";
	public static final String CARGAR_SALVAGUARDAS_POR_RIESGO="cargarSalvaguardasPorRiesgo";

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "proj_id")
    private Integer projId;
	
	@Getter
	@Setter
    @Column(name = "proj_code")
    private String projCode;
	
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
	@Column(name = "proj_register_status")
	private String projRegisterStatus;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsCobenefits> projectsCobenefitsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsSpecificObjectives> projectsSpecificObjectivesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsRisks> projectsRisksList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSafeguardsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectsGenderInfo> projectsGenderInfoList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectQuestions> projectQuestionsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projects", fetch = FetchType.LAZY)
	private List<ProjectUsers> projectUsersList;
	
	@Getter
	@Setter
	@JoinColumn(name = "part_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Partners partners;
	
	@Getter
	@Setter
	@JoinColumn(name = "acpl_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ActionPlans actionPlans;
   
}
