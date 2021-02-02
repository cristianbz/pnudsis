package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsRisks
 *
 */
@Entity
@Table(name = "projects_risks", schema = "sigma")
@NamedQueries({
	
	
})
public class ProjectsRisks implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "prri_id")
	@Id
	@SequenceGenerator(name = "PROJECTSRISKS_GENERATOR", initialValue = 1, sequenceName = "projects_risks_prri_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTSRISKS_GENERATOR")
	private Integer prriId;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "projectsRisks", fetch = FetchType.LAZY)
	private List<ProjectsSafeguards> projectsSafeguardsList;
   
}
