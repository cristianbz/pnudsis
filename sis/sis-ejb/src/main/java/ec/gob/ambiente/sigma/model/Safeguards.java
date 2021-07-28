package ec.gob.ambiente.sigma.model;

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

import ec.gob.ambiente.sis.model.ProjectQuestions;
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
	@NamedQuery(name = Safeguards.CARGAR_TODAS_SALVAGUARDAS,query = "SELECT S FROM Safeguards S WHERE S.safeStatus=true AND S.actionPlans.acplIscurrent=TRUE AND S.actionPlans.acplStatus=TRUE")
	
})
public class Safeguards  {

	
	
	public static final String CARGAR_TODAS_SALVAGUARDAS="cargarTodasSalvaguardas";

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "safe_id")
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
	@Column(name = "safe_code")
	private String safeCode;
	
	@Getter
	@Setter
	@Column(name = "safe_title")
	private String safeTitle;
	 
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
	private List<ProjectQuestions> projectQuestionsList;
	
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
