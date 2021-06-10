package ec.gob.ambiente.sis.model;

import java.util.Date;
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

import ec.gob.ambiente.sigma.model.Safeguards;
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
	@NamedQuery(name = Questions.CARGAR_PREGUNTAS_POR_SALVAGUARDA,query = "SELECT Q FROM Questions Q WHERE Q.safeguards.safeId=:codigoSalvaguarda AND Q.quesStatus=False")
	
})
public class Questions  {

	
	
	public static final String CARGAR_PREGUNTAS="cargarPreguntas";
	public static final String CARGAR_PREGUNTAS_POR_SALVAGUARDA="cargarPreguntasPorSalvaguarda";

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ques_id")
    private Integer quesId;
	
	@Getter
	@Setter
	@Column(name = "ques_parent_id")
	private Integer quesParentId;
	
	@Getter
	@Setter
	@Column(name = "ques_status")
	private boolean quesStatus;
	
	@Getter
	@Setter
	@Column(name = "ques_is_gender")
	private boolean quesIsGender;
	
	@Getter
	@Setter
	@Column(name = "ques_question_order")
	private Integer quesQuestionOrder;
	
	@Getter
	@Setter
	@Column(name="ques_content_question")
	private String	quesContentQuestion;
	
	@Getter
	@Setter
	@Column(name="ques_content_question_english")
	private String	quesContentQuestionEnglish;
	
	@Getter
	@Setter
	@Column(name="ques_creator_user")
	private String quesCreatorUser;
	
	@Getter
	@Setter
	@Column(name="ques_update_user")
	private String quesUpdateUser;
	
	
	@Getter
	@Setter
	@Column(name="ques_creation_date")
	private Date quesCreationDate;
	
	@Getter
	@Setter
	@Column(name="ques_update_date")
	private Date quesUpdateDate;
	
	@Getter
	@Setter
	@JoinColumn(name = "safe_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Safeguards safeguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "cata_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Catalogs catalogs;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "questions")
	private List<ValueAnswers> valueAnswersList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "questions", fetch = FetchType.EAGER)
	private List<TableResponses> tableResponsesList;
   
}
