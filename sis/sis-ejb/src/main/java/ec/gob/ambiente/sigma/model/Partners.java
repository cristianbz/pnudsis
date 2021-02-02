package ec.gob.ambiente.sigma.model;

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
 * Entity implementation class for Entity: Partners
 *
 */
@Entity
@Table(name = "partners", schema = "sigma")
@NamedQueries({
	@NamedQuery(name = Partners.CARGAR_SOCIOS,query = "SELECT PA FROM Partners PA"),
	@NamedQuery(name = Partners.CARGAR_SOCIOS_POR_CODIGO,query = "SELECT PA FROM Partners PA WHERE PA.partId=:codigoSocio")
	
})
public class Partners implements Serializable {

	
	private static final long serialVersionUID = 1L;
	public static final String CARGAR_SOCIOS="cargarSocios";
	public static final String CARGAR_SOCIOS_POR_CODIGO="cargarSociosPorCodigo";

	@Getter
	@Setter
	@Column(name = "part_id")
	@Id
	@SequenceGenerator(name = "PARTNERS_GENERATOR", initialValue = 1, sequenceName = "partners_part_id_seq", schema = "sigma")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTNERS_GENERATOR")
	private Integer partId;
   
	@Getter
	@Setter
	@Column(name = "part_name")
	private String partName;
	
	@Getter
	@Setter
	@Column(name = "part_status")
	private boolean partStatus;
	
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "partners", fetch = FetchType.LAZY)
	private List<Projects> projectsList;
}
