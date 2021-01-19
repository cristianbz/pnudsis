package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Questions
 *
 */
@Entity
@Table(name = "questions", schema = "sis")
@NamedQueries({
	@NamedQuery(name = Questions.CARGAR_PREGUNTAS,query = "SELECT Q FROM Questions Q"),
	@NamedQuery(name = Questions.CARGAR_PREGUNTAS_POR_SALVAGUARDA,query = "SELECT Q FROM Questions Q WHERE Q.safeguards.safeId=:codigoSalvaguarda")
})
public class Questions implements Serializable {

	
	private static final long serialVersionUID = 1L;
	public static final String CARGAR_PREGUNTAS="cargarPreguntas";
	public static final String CARGAR_PREGUNTAS_POR_SALVAGUARDA="cargarPreguntasPorSalvaguarda";

	@Getter
	@Setter
	@Column(name = "ques_id")
	@Id
	@SequenceGenerator(name = "QUESTIONS_GENERATOR", initialValue = 1, sequenceName = "questions_ques_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONS_GENERATOR")
	private Integer quesId;
	
	@Getter
	@Setter
	@Column(name = "ques_parent_id")
	private Integer quesParentId;
	
	@Getter
	@Setter
	@Column(name = "ques_drop_state")
	private boolean quesDropState;
	
	@Getter
	@Setter
	@Column(name = "ques_question_order")
	private Integer quesQuestionOrder;
	
	@Getter
	@Setter
	@JoinColumn(name = "safe_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Safeguards safeguards;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "questions", fetch = FetchType.LAZY)
	private List<QuestionsAnswers> questionsAnswersList;
   
}
