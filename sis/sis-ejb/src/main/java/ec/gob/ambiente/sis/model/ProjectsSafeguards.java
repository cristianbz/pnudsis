package ec.gob.ambiente.sis.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectSafeguards
 *
 */
@Entity
@Table(name = "projects_safeguards", schema = "sis")
@NamedQueries({
	@NamedQuery(name = ProjectsSafeguards.CARGAR_SALVAGUARDAS_POR_PROYECTO,query = "SELECT PS FROM ProjectsSafeguards PS WHERE PS.projects.projId=:codigoProyecto")	
})
public class ProjectsSafeguards  {

	
	
	public static final String CARGAR_SALVAGUARDAS_POR_PROYECTO="cargarSalvaguardasPorProyecto";

	@Getter
	@Setter
	@Column(name = "prsa_id")
	@Id
	@SequenceGenerator(name = "PROJECTSSAVEGUARDS_GENERATOR", initialValue = 1, sequenceName = "projects_safeguards_safe_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTSSAVEGUARDS_GENERATOR")
	private Integer prsaId;
   
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
}
