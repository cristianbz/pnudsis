package ec.gob.ambiente.sis.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ValueAnswers
 *
 */
@Entity
@Table(name = "value_answers", schema = "sis")
@NamedQueries({
	@NamedQuery(name = ValueAnswers.CARGA_POR_AVANCE_EJECUCION,query = "SELECT VA FROM ValueAnswers VA WHERE VA.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND VA.advanceExecutionSaveguards.adexIsReported=TRUE")	
})
public class ValueAnswers  {

	
	public static final String CARGA_POR_AVANCE_EJECUCION="cargaPorAvanceEjecucion";

	@Getter
	@Setter
	@Column(name = "vaan_id")
	@Id
	@SequenceGenerator(name = "VALUEANSWERS_GENERATOR", initialValue = 1, sequenceName = "value_answers_vaan_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VALUEANSWERS_GENERATOR")
	private Integer vaanId;
	
	@Getter
	@Setter
	@Column(name = "vaan_yesno_answer")
	private boolean vaanYesnoAnswer;
	
	@Getter
	@Setter
	@Column(name = "vaan_text_answer_value")
	private String vaanTextAnswerValue;
	
	@Getter
	@Setter
	@Column(name = "vaan_numeric_answer_value")
	private Integer vaanNumericAnswerValue;
	
	@Getter
	@Setter
	@Column(name = "vaan_remove_state")
	private boolean vaanRemoveState;
	
	
	@Getter
	@Setter
	@JoinColumn(name = "quan_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private QuestionsAnswers questionsAnswers;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
   
}
