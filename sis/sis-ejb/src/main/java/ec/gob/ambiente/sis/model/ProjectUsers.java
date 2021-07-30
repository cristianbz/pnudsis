package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import javax.persistence.*;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.suia.model.Users;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectUsers
 *
 */
@Entity
@Table(name = "project_users", schema = "sis")
@NamedQueries({
	@NamedQuery(name = "q3",query = "SELECT PU FROM ProjectUsers PU WHERE PU.users.userId = :codigoUsuario AND PU.prusStatus=TRUE")	
})
public class ProjectUsers implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prus_id")
    private Integer prusId;
	
	@Getter
	@Setter
	@Column(name = "prus_status")
	private boolean prusStatus;
	
	@Getter
	@Setter
    @Column(name = "pspa_id")
    private Integer pspaId;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Projects projects;
	
	@Getter
	@Setter
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Users users;
	
	@Transient
	@Getter
	@Setter
	private String prusRolUsuario;
	
	@Transient
	@Getter
	@Setter
	private String prusPartner;
   
}
