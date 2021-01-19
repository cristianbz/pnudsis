package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Answers
 *
 */
@Entity
@Table(name = "answers", schema = "sis")
@NamedQueries({
	@NamedQuery(name = Answers.CARGAR_TODAS_PREGUNTAS,query = "SELECT A FROM Answers A")
	
})
public class Answers implements Serializable {

	
	private static final long serialVersionUID = 1L;
	public static final String CARGAR_TODAS_PREGUNTAS="cargarTodasPreguntas";
	
	@Getter
	@Setter
	@Column(name = "answ_id")
	@Id
	@SequenceGenerator(name = "ANSWERS_GENERATOR", initialValue = 1, sequenceName = "answers_answ_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWERS_GENERATOR")
	private Integer answId;
	
	@Getter
	@Setter
	@Column(name="answ_answer_label")
	private String answAnswerLabel;
	
	@Getter
	@Setter
	@Column(name="answ_response_type_format")
	private String answResponseTypeFormat;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "answers", fetch = FetchType.LAZY)
	private List<QuestionsAnswers> questionsAnswersList;
	
}
