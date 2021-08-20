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

import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsStrategicPartners
 *
 */
@Entity
@Table(name = "projects_strategic_partners", schema = "sigma")
@NamedQueries({
	@NamedQuery(name = "erss",query = "SELECT PSP FROM ProjectsStrategicPartners PSP WHERE PSP.projects.projId=1")	
})
public class ProjectsStrategicPartners implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pspa_id")
    private Integer pspaId;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Projects projects;
	
	@Getter
	@Setter
	@JoinColumn(name = "part_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Partners partners;
	
	@Getter
	@Setter
	@Column(name = "pspa_status")
	private boolean pspaStatus;
	
	@Getter
	@Setter
	@Column(name = "pspa_type")
	private String pspaType;
	
	@Getter
	@Setter
	@Column(name = "pspa_has_project")
	private Boolean pspaHasProject;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projectsStrategicPartners")
	private List<ProjectQuestions> projectQuestionsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projectsStrategicPartners", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSafeguardsList;
   
}
