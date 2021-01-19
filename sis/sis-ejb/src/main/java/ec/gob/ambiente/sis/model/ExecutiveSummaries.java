package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ExecutiveSummaries
 *
 */
@Entity
@Table(name = "executive_summaries", schema = "sis")
public class ExecutiveSummaries implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "exsu_id")
	@Id
	@SequenceGenerator(name = "EXECUTIVESUMMARIES_GENERATOR", initialValue = 1, sequenceName = "executive_summaries_exsu_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXECUTIVESUMMARIES_GENERATOR")
	private Integer exsu_id;
	
	@Getter
	@Setter
	@Column(name = "exsu_register_date")
	private Date exsuRegisterDate;
	
	@Getter
	@Setter
	@Column(name = "exsu_summary_content")
	private String exsuSummaryContent;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
   
}
