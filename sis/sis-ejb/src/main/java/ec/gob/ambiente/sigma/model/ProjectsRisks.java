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
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prri_id")
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
