package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ActionPlans
 *
 */
@Entity

public class ActionPlans implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "acpl_id")
	@Id
	@SequenceGenerator(name = "ACTIONPLANS_GENERATOR", initialValue = 1, sequenceName = "action_plans_acpl_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIONPLANS_GENERATOR")
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
	@OneToMany(mappedBy = "actionPlans", fetch = FetchType.LAZY)
	private List<Safeguards> safeguardsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "actionPlans", fetch = FetchType.LAZY)
	private List<Projects> projectsList;
   
}
