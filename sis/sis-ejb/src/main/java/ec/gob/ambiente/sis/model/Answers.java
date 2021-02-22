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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "answ_id")
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
	@OneToMany(mappedBy = "answers", fetch = FetchType.LAZY)
	private List<QuestionsAnswers> questionsAnswersList;
	
	@Getter
	@Setter
    @JoinColumn(name = "cata_id", referencedColumnName = "cata_id")
    @ManyToOne(optional = false)
    private Catalogs cataId;

}
