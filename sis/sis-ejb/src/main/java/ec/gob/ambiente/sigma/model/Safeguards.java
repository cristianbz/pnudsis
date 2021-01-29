package ec.gob.ambiente.sigma.model;

import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ec.gob.ambiente.sis.model.Questions;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Safewards
 *
 */
@Entity
@Table(name = "safeguards", schema = "sigma")
@NamedQueries({
	@NamedQuery(name = Safeguards.CARGAR_TODAS_SALVAGUARDAS,query = "SELECT S FROM Safeguards S WHERE S.safeStatus=true")
	
})
public class Safeguards  {

	
	
	public static final String CARGAR_TODAS_SALVAGUARDAS="cargarTodasSalvaguardas";

	@Getter
	@Setter
	@Column(name = "safe_id")
	@Id
	@SequenceGenerator(name = "SAFEGUARDS_GENERATOR", initialValue = 1, sequenceName = "safeguards_safe_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAFEGUARDS_GENERATOR")
	private Integer safeId;
	
	@Getter
	@Setter
	@Column(name = "safe_parent_id")
	private Integer safeParentId;
	

	
	@Getter
	@Setter
	@Column(name = "safe_description")
	private String safeDescription;
	
	@Getter
	@Setter
	@Column(name = "safe_order")
	private Integer safeOrder;
	
	@Getter
	@Setter
	@Column(name = "safe_level")
	private Integer safeLevel;
	
	@Getter
	@Setter
	@Column(name = "safe_status")
	private boolean safeStatus;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "safeguards", fetch = FetchType.LAZY)
	private List<Questions> questionsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "safeguards", fetch = FetchType.LAZY)
	private List<ProjectsSafeguards> projectsSafeguardsList;
	
	@Getter
	@Setter
	@JoinColumn(name = "acpl_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ActionPlans actionPlans;
	
}
