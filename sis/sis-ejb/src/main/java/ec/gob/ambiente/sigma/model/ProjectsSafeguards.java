package ec.gob.ambiente.sigma.model;

import java.io.Serializable;

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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectsSafeguards
 *
 */
@Entity
@Table(name = "projects_safeguards", schema = "sigma")
@NamedQueries({
	
	
})
public class ProjectsSafeguards implements Serializable {

	
	private static final long serialVersionUID = 1L;
	

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prsa_id")
    private Integer prsaId;
	
	@Getter
	@Setter
	@Column(name = "prsa_status")
	private boolean prsaStatus;
	
	@Getter
	@Setter
	@JoinColumn(name = "prco_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ProjectsCobenefits projectsCobenefits;
	
	@Getter
	@Setter
	@JoinColumn(name = "safe_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Safeguards safeguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "prri_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ProjectsRisks projectsRisks;
	
}
