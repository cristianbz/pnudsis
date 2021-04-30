package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "part_id")
    private Integer partId;
   
	@Getter
	@Setter
	@Column(name = "part_name")
	private String partName;
	
	@Getter
	@Setter
	@Column(name = "part_status")
	private boolean partStatus;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "part_type")
    private String partType;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "partners", fetch = FetchType.LAZY)
	private List<Projects> projectsList;
}
