package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsCobenefits
 *
 */
@Entity
@Table(name = "projects_cobenefits", schema = "sigma")
@NamedQueries({
	
	
})
public class ProjectsCobenefits implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "prco_id")
	@Id
	@SequenceGenerator(name = "PROJECTSCOBENEFITS_GENERATOR", initialValue = 1, sequenceName = "projects_cobenefits_prco_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTSCOBENEFITS_GENERATOR")
	private Integer prcoId;
	
	@Getter
	@Setter
	@Column(name="prco_status")
	private boolean prcoStatus;
	
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projectsCobenefits", fetch = FetchType.LAZY)
	private List<ProjectsSafeguards> projectsSafeguardsList;
	
   
}
