package ec.gob.ambiente.sis.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ExecutiveSummaries
 *
 */
@Entity
@Table(name = "executive_summaries", schema = "sis")
public class ExecutiveSummaries  {

	
	

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "exsu_id")
    private Integer exsuId;
	
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
	@Column(name="exsu_creator_user")
	private String exsuCreatorUser;
	
	@Getter
	@Setter
	@Column(name="exsu_update_user")
	private String exsuUpdateUser;
	
	
	@Getter
	@Setter
	@Column(name="exsu_creation_date")
	private Date exsuCreationDate;
	
	@Getter
	@Setter
	@Column(name="exsu_update_date")
	private Date exsuUpdateDate;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
   
}
