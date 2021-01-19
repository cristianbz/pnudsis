package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
public class AdvanceExecutionSafeguards implements Serializable {

	
	private static final long serialVersionUID = 1L;
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
