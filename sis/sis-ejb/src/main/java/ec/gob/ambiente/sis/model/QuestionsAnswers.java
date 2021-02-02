package ec.gob.ambiente.sis.model;

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

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: QuestionsAnswers
 *
 */
@Entity
@Table(name = "questions_answers", schema = "sis")
@NamedQueries({
	@NamedQuery(name = QuestionsAnswers.CARGA_PREGUNTAS_RESPUESTAS_POR_SALVAGUARDA,query = "SELECT QA FROM QuestionsAnswers QA WHERE QA.quanRemoveState=TRUE AND QA.questions.safeguards.safeId IN(:codigoSalvaguarda) ORDER BY QA.questions.quesQuestionOrder")	
})
public class QuestionsAnswers  {

	
	public static final String CARGA_PREGUNTAS_RESPUESTAS_POR_SALVAGUARDA="cargaPreguntasRespuestasPorSalvaguarda"; 

	@Getter
	@Setter
	@Column(name = "quan_id")
	@Id
	@SequenceGenerator(name = "QUESTIONSANSWERS_GENERATOR", initialValue = 1, sequenceName = "questions_answers_quan_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONSANSWERS_GENERATOR")
	private Integer quanId;
	
	@Getter
	@Setter
	@Column(name = "quan_remove_state")
	private boolean quanRemoveState;
	
	@Getter
	@Setter
	@JoinColumn(name = "answ_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Answers answers;
	
	@Getter
	@Setter
	@JoinColumn(name = "ques_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Questions questions;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "questionsAnswers", fetch = FetchType.LAZY)
	private List<TableResponses> tableResponsesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "questionsAnswers", fetch = FetchType.LAZY)
	private List<ValueAnswers> valueAnswersList;
}
