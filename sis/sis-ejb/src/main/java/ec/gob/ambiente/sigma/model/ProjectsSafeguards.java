package ec.gob.ambiente.sigma.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsSafeguards
 *
 */
@Entity

public class ProjectsSafeguards implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "prsa_id")
	@Id
	@SequenceGenerator(name = "PROJECTSSAVEGUARDS_GENERATOR", initialValue = 1, sequenceName = "projects_safeguards_prsa_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTSSAVEGUARDS_GENERATOR")
	private Integer prsaId;
	
	@Getter
	@Setter
	@JoinColumn(name = "prco_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ProjectsCobenefits projectsCobenefits;
	
	@Getter
	@Setter
	@JoinColumn(name = "prco_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Safeguards safeguards;
	
}
