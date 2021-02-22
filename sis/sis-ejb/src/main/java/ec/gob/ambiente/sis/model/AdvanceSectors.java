package ec.gob.ambiente.sis.model;

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
 * Entity implementation class for Entity: ApproachSectors
 *
 */
@Entity
@Table(name = "advance_sectors", schema = "sis")
public class AdvanceSectors  {

	
	

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "adse_id")
    private Integer adseId;
	
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "sect_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Sectors sectors;
   
}
