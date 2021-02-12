package ec.gob.ambiente.sis.model;

import java.io.Serializable;
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
 * Entity implementation class for Entity: Sectors
 *
 */
@Entity
@Table(name = "sectors", schema = "sis")
@NamedQueries({
	@NamedQuery(name = Sectors.CARGA_SECTORES,query = "SELECT S FROM Sectors S WHERE S.sectDropState=true")	
})
public class Sectors implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final String CARGA_SECTORES="cargaSectores";
	

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
	@Column(name = "sect_drop_state")
	private boolean sectDropState;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "sectors", fetch = FetchType.LAZY)
	private List<AdvanceSectors> advanceSectorsList;
}
