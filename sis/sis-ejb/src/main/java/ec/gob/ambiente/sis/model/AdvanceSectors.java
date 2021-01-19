package ec.gob.ambiente.sis.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ApproachSectors
 *
 */
@Entity
@Table(name = "advance_sectors", schema = "sis")
public class AdvanceSectors implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "adse_id")
	@Id
	@SequenceGenerator(name = "ADVANCESECTORS_GENERATOR", initialValue = 1, sequenceName = "advance_sectors_adse_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADVANCESECTORS_GENERATOR")
	private Integer adse_id;
	
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "sect_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Sectors sectors;
   
}
