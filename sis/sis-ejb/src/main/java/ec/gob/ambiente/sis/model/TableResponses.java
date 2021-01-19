package ec.gob.ambiente.sis.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: TableResponses
 *
 */
@Entity
@Table(name = "table_responses", schema = "sis")
public class TableResponses implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "tare_id")
	@Id
	@SequenceGenerator(name = "TABLERESPONSES_GENERATOR", initialValue = 1, sequenceName = "table_responses_tare_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABLERESPONSES_GENERATOR")
	private Integer tareId;
	
	@Getter
	@Setter
	@Column(name = "tare_column_one")
	private String tareColumnOne;
	
	@Getter
	@Setter
	@Column(name = "tare_column_two")
	private String tareColumnTwo;
	
	@Getter
	@Setter
	@Column(name = "tare_column_three")
	private String tareColumnTree;
	
	@Getter
	@Setter
	@Column(name = "tare_column_four")
	private String tareColumnFour;
	
	@Getter
	@Setter
	@Column(name = "tare_column_five")
	private String tareColumnFive;
	
	@Getter
	@Setter
	@Column(name = "tare_column_six")
	private String tareColumnSix;
	
	@Getter
	@Setter
	@Column(name = "tare_column_seven")
	private String tareColumnSeven;
	
	@Getter
	@Setter
	@Column(name = "tare_remove_state")
	private boolean tareRemoveState;
	
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
