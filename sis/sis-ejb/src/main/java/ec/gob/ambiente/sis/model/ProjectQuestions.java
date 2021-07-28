package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import javax.persistence.*;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectQuestions
 *
 */
@Entity
@Table(name = "project_questions", schema = "sis")
@NamedQueries({
	@NamedQuery(name = "q1",query = "SELECT PQ FROM ProjectQuestions PQ WHERE PQ.projectsStrategicPartners.pspaId =1 AND PQ.prquStatus=TRUE")	
})
public class ProjectQuestions implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prqu_id")
    private Integer prquId;
	
	@Getter
	@Setter
	@Column(name = "prqu_status")
	private boolean prquStatus;
   
	@Getter
	@Setter
	@JoinColumn(name = "cata_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Catalogs catalogs;
	
	@Getter
	@Setter
	@JoinColumn(name = "pspa_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private ProjectsStrategicPartners projectsStrategicPartners;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Projects projects;
	
	@Getter
	@Setter
	@JoinColumn(name = "safe_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Safeguards safeguards;
}
