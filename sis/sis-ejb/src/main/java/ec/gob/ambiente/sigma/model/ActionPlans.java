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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ActionPlans
 *
 */
@Entity
@Table(name = "action_plans", schema = "sigma")
public class ActionPlans implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acpl_id")
    private Integer acplId;
	
	@Getter
	@Setter
	@Column(name = "acpl_name")
	private String acplName;
	
	@Getter
	@Setter
	@Column(name = "acpl_iscurrent")
	private boolean acplIscurrent;
	
	@Getter
	@Setter
	@Column(name = "acpl_status")
	private boolean acplStatus;
	
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "actionPlans", fetch = FetchType.LAZY)
	private List<Safeguards> safeguardsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "actionPlans", fetch = FetchType.LAZY)
	private List<Projects> projectsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "actionPlans", fetch = FetchType.LAZY)
	private List<Components> componentsList;
   
}
