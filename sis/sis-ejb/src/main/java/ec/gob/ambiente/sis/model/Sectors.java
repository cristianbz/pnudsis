package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Sectors
 *
 */
@Entity
@Table(name = "sectors", schema = "sis")
public class Sectors implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(name = "sect_id")
	@Id
	@SequenceGenerator(name = "SECTORS_GENERATOR", initialValue = 1, sequenceName = "sectors_sect_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECTORS_GENERATOR")
	private Integer sect_id;
   
	@Getter
	@Setter
	@Column(name = "sect_name")
	private String sectName;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "sectors", fetch = FetchType.LAZY)
	private List<AdvanceSectors> advanceSectorsList;
}
