package ec.gob.ambiente.sis.model;

import java.util.Date;
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

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.suia.model.Users;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: AdvanceExecutionSaveguards
 *
 */
@Entity
@Table(name = "advance_execution_safeguards", schema = "sis")
@NamedQueries({
	@NamedQuery(name = AdvanceExecutionSafeguards.CARGAR_AVANCE_POR_PROYECTO,query = "SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto")	
})
public class AdvanceExecutionSafeguards {

	
	
	public static final String CARGAR_AVANCE_POR_PROYECTO="cargarAvancePorProyecto";

	@Getter
	@Setter
	@Column(name = "adex_id")
	@Id
	@SequenceGenerator(name = "ADVANCEEXECUTIONSAFEGUARDS_GENERATOR", initialValue = 1, sequenceName = "advance_execution_safeguards_adex_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADVANCEEXECUTIONSAFEGUARDS_GENERATOR")
	private Integer adexId;
	
	@Getter
	@Setter
	@Column(name = "adex_register_date")
	private Date adexRegisterDate;
	
	@Getter
	@Setter
	@Column(name = "adex_is_reported")
	private boolean adexIsReported;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<ValueAnswers> valueAnswersList;
   
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<TableResponses> tableResponsesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<ExecutiveSummaries> executiveSummariesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<AdvanceSectors> advanceSectorsList;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
	
	@Getter
	@Setter
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Users users;
}
