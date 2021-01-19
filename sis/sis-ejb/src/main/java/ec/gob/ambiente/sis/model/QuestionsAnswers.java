package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: QuestionsAnswers
 *
 */
@Entity
@Table(name = "questions_answers", schema = "sis")
public class QuestionsAnswers implements Serializable {

	
	private static final long serialVersionUID = 1L;

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
