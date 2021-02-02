package ec.gob.ambiente.sis.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
public class Answers  {

	
	
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
	@Column(name="answ_drop_state")
	private boolean answDropState;
	
	@Getter
	@Setter
	@Column(name="answ_creator_user")
	private String answCreatorUser;
	
	@Getter
	@Setter
	@Column(name="answ_update_user")
	private String answUpdateUser;
	
	
	@Getter
	@Setter
	@Column(name="answ_creation_date")
	private Date answCreationDate;
	
	@Getter
	@Setter
	@Column(name="answ_update_date")
	private Date answUpdateDate;
	
	
	@Getter
	@Setter
	@Column(name="answ_response_type_format")
	private String answResponseTypeFormat;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "answers", fetch = FetchType.LAZY)
	private List<QuestionsAnswers> questionsAnswersList;
	
}
